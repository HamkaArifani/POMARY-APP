package com.example.pomaryapp.core.utils

import android.content.Context
import android.widget.Toast
import com.example.pomaryapp.R
import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.model.PreorderModel

object PdfGenerator {
    fun export(context: Context, preorder: PreorderModel, orders: List<OrderModel>) {
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

        pdf.finishPage(page)
        val file = java.io.File(context.getExternalFilesDir(null), "${preorder.title}.pdf")
        pdf.writeTo(file.outputStream())
        pdf.close()
        Toast.makeText(context, context.getString(R.string.success_save), Toast.LENGTH_SHORT).show()
    }
}