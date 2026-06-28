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
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            val uid = auth.currentUser?.uid ?: return

            val remoteData = firestore.collection(Constants.COLL_USERS)
                .document(uid)
                .collection(Constants.COLL_PREORDERS)
                .get()
                .await()

            remoteData.documents.forEach { doc ->
                val dto = doc.toObject(PreorderDto::class.java)
                if (dto != null){
                    preorderDao.insertPreorder(dto.toEntity())

                    val remoteOrders = doc.reference.collection(Constants.COLL_ORDERS).get().await()
                    remoteOrders.documents.forEach { orderDoc->
                        val orderDto = orderDoc.toObject(OrderDto::class.java)
                        orderDto?. let { orderDao.insertOrder(it.toEntity()) }
                    }
                }
            }
            Timber.d("Sinkronisasi berhasil: ${remoteData.size()} preorder diunduh.")
        }catch (e: Exception){
            Timber.e(e, "Sinkronisasi data gagal!")
        }

    }
}