package com.example.pomaryapp.data.repository

import com.example.pomaryapp.core.utils.Constants
import com.example.pomaryapp.data.local.dao.OrderDao
import com.example.pomaryapp.data.local.dao.PreorderDao
import com.example.pomaryapp.data.mapper.toDomain
import com.example.pomaryapp.data.mapper.toDto
import com.example.pomaryapp.data.mapper.toEntity
import com.example.pomaryapp.data.remote.dto.OrderDto
import com.example.pomaryapp.data.remote.dto.PreorderDto
import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.model.PreorderModel
import com.example.pomaryapp.domain.repository.PreorderRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreorderRepositoryImpl @Inject constructor(
    private val preorderDao: PreorderDao,
    private val orderDao: OrderDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): PreorderRepository{
    private val userDoc get() = auth.currentUser?.uid?.let {
        firestore.collection(Constants.COLL_USERS).document(it)
    }

    override fun getActivePreorders(): Flow<List<PreorderModel>> {
        return preorderDao.getAllPreorders().map { list->
            list.filter { !it.isCompleted }.map { it.toDomain() }
        }
    }

    override suspend fun getActivePreordersSync(): List<PreorderModel> {
        return preorderDao.getAllPreordersSync()
            .filter { !it.isCompleted }
            .map { it.toDomain() }
    }

    override fun getCompletedPreorders(): Flow<List<PreorderModel>> {
        return preorderDao.getAllPreorders().map { list->
            list.filter { it.isCompleted }.map { it.toDomain() }
        }
    }

    override suspend fun getPreordersById(id: String): PreorderModel? {
        return preorderDao.getPreorderById(id)?.toDomain()
    }

    override suspend fun insertPreorder(preorder: PreorderModel) {
        try {
            preorderDao.insertPreorder(preorder.toEntity())
            try {
                userDoc?.collection(Constants.COLL_PREORDERS)
                    ?.document(preorder.preorderId)
                    ?.set(preorder.toDto())
            } catch (f: Exception) { Timber.w("Firebase Offline") }
        } catch (e: Exception) {
            Timber.e(e, "Gagal insert preorder")
        }
    }

    override suspend fun updatePreorder(preorder: PreorderModel) {
        try {
            preorderDao.updatePreorder(preorder.toEntity())
            try {
                userDoc?.collection(Constants.COLL_PREORDERS)
                    ?.document(preorder.preorderId)
                    ?.set(preorder.toDto())
            } catch (f: Exception) { Timber.w("Firebase Offline") }
        } catch (e: Exception) {
            Timber.e(e, "Gagal update preorder")
        }
    }

    override suspend fun deletePreorder(preorder: PreorderModel) {
        try {
            preorderDao.deletePreorder(preorder.toEntity())
            userDoc?.collection(Constants.COLL_PREORDERS)
                ?.document(preorder.preorderId)
                ?.delete()
        }catch (e: Exception){
            Timber.e(e, "Gagal delete preorder")
        }
    }

    override fun getOrdersByPreorderId(preorderId: String): Flow<List<OrderModel>> {
        return orderDao.getOrdersByPreorder(preorderId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getOrdersByPreorderIdRealtime(preorderId: String): Flow<List<OrderModel>> = callbackFlow {
        val uid = auth.currentUser?.uid ?: run { close(); return@callbackFlow }
        Timber.d("REALTIME: Listener dipasang untuk preorderId=$preorderId uid=$uid")

        val listener = firestore
            .collection(Constants.COLL_USERS)
            .document(uid)
            .collection(Constants.COLL_PREORDERS)
            .document(preorderId)
            .collection(Constants.COLL_ORDERS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                snapshot?.let { result ->
                    val entities = result.documents.mapNotNull { doc ->
                        doc.toObject(OrderDto::class.java)?.toEntity()
                    }
                    launch { entities.forEach { orderDao.insertOrder(it) } }
                    trySend(entities.map { it.toDomain() })
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getOrderById(orderId: String): OrderModel? {
        return try {
            val entity = orderDao.getOrderById(orderId)
            entity?.toDomain()
        } catch (e: Exception) {
            Timber.e(e, "Gagal mengambil detail order dengan ID: $orderId")
            null
        }
    }

    override suspend fun insertOrder(order: OrderModel) {
        try {
            orderDao.insertOrder(order.toEntity())
            val allOrders = orderDao.getOrdersByPreorderSync(order.preorderId)
            val newTotalQuantity = allOrders.sumOf { it.buyerQuantity }
            val currentPo = preorderDao.getPreorderById(order.preorderId)
            currentPo?.let {
                val updatedPo = it.copy(totalOrders = newTotalQuantity)
                preorderDao.updatePreorder(updatedPo)

                try {
                    userDoc?.collection(Constants.COLL_PREORDERS)
                        ?.document(it.preorderId)
                        ?.set(updatedPo.toDomain().toDto())

                    userDoc?.collection(Constants.COLL_PREORDERS)
                        ?.document(order.preorderId)
                        ?.collection(Constants.COLL_ORDERS)
                        ?.document(order.orderId)
                        ?.set(order.toDto())
                }catch (firebaseError: Exception) {
                    Timber.w("Firebase Offline: Data tersimpan di lokal saja")
                    Timber.e(firebaseError)
                }
            }
        }catch (e: Exception){
            Timber.e(e, "Gagal insert order")
        }
    }

    override suspend fun updateOrder(order: OrderModel) {
        try {
            orderDao.updateOrder(order.toEntity())
            val allOrders = orderDao.getOrdersByPreorderSync(order.preorderId)
            val newTotalQuantity = allOrders.sumOf { it.buyerQuantity }
            val currentPo = preorderDao.getPreorderById(order.preorderId)
            currentPo?.let {
                val updatedPo = it.copy(totalOrders = newTotalQuantity)
                preorderDao.updatePreorder(updatedPo)

                try {
                    userDoc?.collection(Constants.COLL_PREORDERS)
                        ?.document(it.preorderId)
                        ?.set(updatedPo.toDomain().toDto())

                    userDoc?.collection(Constants.COLL_PREORDERS)
                        ?.document(order.preorderId)
                        ?.collection(Constants.COLL_ORDERS)
                        ?.document(order.orderId)
                        ?.set(order.toDto())
                } catch (firebaseError: Exception) {
                    Timber.w("Firebase Offline: Update hanya di lokal")
                }
            }
        }catch (e: Exception){
            Timber.e(e, "Gagal update order")
        }
    }

    override suspend fun deleteOrder(order: OrderModel) {
        try {
            orderDao.deleteOrder(order.toEntity())
            val allOrders = orderDao.getOrdersByPreorderSync(order.preorderId)
            val newTotal = allOrders.sumOf { it.buyerQuantity }

            preorderDao.getPreorderById(order.preorderId)?.let { po ->
                val updatedPo = po.copy(totalOrders = newTotal)
                preorderDao.updatePreorder(updatedPo)

                userDoc?.collection(Constants.COLL_PREORDERS)
                    ?.document(po.preorderId)?.set(updatedPo.toDomain().toDto())
            }

            userDoc?.collection(Constants.COLL_PREORDERS)
                ?.document(order.preorderId)
                ?.collection(Constants.COLL_ORDERS)
                ?.document(order.orderId)?.delete()
        } catch (e: Exception) {
            Timber.e(e, "Gagal delete order")
        }
    }

    override suspend fun syncWithRemote() {
        try {
            val uid = auth.currentUser?.uid ?: throw IllegalStateException("User belum terautentikasi")

            val remoteData = firestore.collection(Constants.COLL_USERS)
                .document(uid)
                .collection(Constants.COLL_PREORDERS)
                .get(Source.SERVER)
                .await()

            if (remoteData.isEmpty) {
                Timber.d("SYNC_CHECK: Tidak ada data preorder ditemukan di Firestore.")
                return
            }

            val remotePreorderIds = mutableSetOf<String>()
            val remoteOrderIds = mutableSetOf<String>()

            remoteData.documents.forEach { doc ->
                val dto = doc.toObject(PreorderDto::class.java) ?: return@forEach
                remotePreorderIds.add(dto.preorderId)

                    preorderDao.insertPreorder(dto.toEntity())
                    Timber.d("SYNC_ORDERS: Memeriksa orderan untuk Preorder ID: ${dto.preorderId}")

                    val remoteOrders = doc.reference
                        .collection(Constants.COLL_ORDERS)
                        .get(Source.SERVER)
                        .await()

                    Timber.d("SYNC_ORDERS: Jumlah orderan ditemukan di Firestore = ${remoteOrders.size()}")
                    remoteOrders.documents.forEach orderLoop@{ orderDoc->
                        val orderDto = orderDoc.toObject(OrderDto::class.java) ?: return@orderLoop
                        remoteOrderIds.add(orderDto.orderId)
                        orderDao.insertOrder(orderDto.toEntity())
                    }
                }
            val localPreorders = preorderDao.getAllPreordersSync()
            localPreorders.forEach { local ->
                if (local.preorderId !in remotePreorderIds) {
                    preorderDao.deletePreorderById(local.preorderId)
                }
            }

            val localOrders = orderDao.getAllOrdersSync()
            localOrders.forEach { local ->
                if (local.orderId !in remoteOrderIds) {
                    orderDao.deleteOrderById(local.orderId)
                }
            }
            Timber.d("SYNC_CHECK: Sinkronisasi berhasil! ${remoteData.size()} preorder diunduh ke Room.")
        } catch (e: Exception) {
            Timber.e(e, "SYNC_CHECK: Terjadi kesalahan fatal saat sinkronisasi!")
            throw e
        }

    }
}