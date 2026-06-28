package com.example.pomaryapp.core.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import com.example.pomaryapp.R
import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.model.PreorderModel

object PdfGenerator {
    fun export(context: Context, uri: Uri, preorder: PreorderModel, orders: List<OrderModel>) {
        val pdf = android.graphics.pdf.PdfDocument()
        val page = pdf.startPage(android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create())
        val canvas = page.canvas
        val paint = android.graphics.Paint()

        var y = 50f
        canvas.drawText(context.getString(R.string.detail_about), 50f, y, paint.apply { textSize = 20f })

        y += 40f
        paint.textSize = 12f
        canvas.drawText("${context.getString(R.string.preorder_title)} ${preorder.title}", 50f, y, paint)
        y += 20f
        canvas.drawText("${context.getString(R.string.initial_capital)} ${preorder.initialCapital.toRupiah()}", 50f, y, paint)
        y += 20f
        canvas.drawText("${context.getString(R.string.gross_profit)} ${preorder.totalGrossProfit.toRupiah()}", 50f, y, paint)
        y += 20f
        canvas.drawText("${context.getString(R.string.clean_profit)} ${preorder.totalCleanProfit.toRupiah()}", 50f, y, paint)

        y += 30f
        canvas.drawLine(50f, y, 545f, y, paint)
        y += 30f

        paint.isFakeBoldText = true
        canvas.drawText(context.getString(R.string.order_list_title), 50f, y, paint)
        y += 20f
        paint.isFakeBoldText = false

        orders.forEachIndexed { index, order ->
            if (y > 800f) return@forEachIndexed
            canvas.drawText("${index + 1}. ${order.buyerName} (${order.buyerQuantity}) - ${order.totalPrice.toRupiah()}", 50f, y, paint)
            y += 20f
        }

        pdf.finishPage(page)
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                pdf.writeTo(outputStream)
            }
            Toast.makeText(context, context.getString(R.string.success_save), android.widget.Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.failed_to_save), android.widget.Toast.LENGTH_SHORT).show()
        } finally {
            pdf.close()
        }
    }
}