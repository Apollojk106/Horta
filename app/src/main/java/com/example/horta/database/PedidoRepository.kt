package com.example.horta.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.horta.utilities.DatabaseHelper

class PedidoRepository(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper(context).readableDatabase

    data class Pedido(
        val id: Long,
        val dataPedido: String,
        val dataEntrega: String,
        val total: Double,
        val status: String
    )

    data class ItemPedido(
        val nomeProduto: String,
        val quantidade: Int,
        val precoUnitario: Double,
        val subtotal: Double
    )

    fun getPedidosByCliente(idCliente: Long): List<Pedido> {
        val pedidos = mutableListOf<Pedido>()

        val query = """
            SELECT p.id_pedido, p.dataPedido, p.dataEntrega, 
                   SUM(pp.quantidade * pp.preco) as total
            FROM pedido p
            LEFT JOIN pedido_produto pp ON p.id_pedido = pp.id_pedido
            WHERE p.id_cliente = ?
            GROUP BY p.id_pedido
            ORDER BY p.dataPedido DESC
        """

        val cursor = db.rawQuery(query, arrayOf(idCliente.toString()))

        while (cursor.moveToNext()) {
            pedidos.add(
                Pedido(
                    id = cursor.getLong(0),
                    dataPedido = cursor.getString(1),
                    dataEntrega = cursor.getString(2) ?: "A definir",
                    total = cursor.getDouble(3),
                    status = if (cursor.getString(2) != null && cursor.getString(2) < getCurrentDate()) "Entregue" else "Em andamento"
                )
            )
        }
        cursor.close()
        return pedidos
    }

    fun getItensDoPedido(idPedido: Long): List<ItemPedido> {
        val itens = mutableListOf<ItemPedido>()

        val query = """
            SELECT pr.nome, pp.quantidade, pp.preco,
                   (pp.quantidade * pp.preco) as subtotal
            FROM pedido_produto pp
            JOIN produto pr ON pp.id_produto = pr.id_produto
            WHERE pp.id_pedido = ?
        """

        val cursor = db.rawQuery(query, arrayOf(idPedido.toString()))

        while (cursor.moveToNext()) {
            itens.add(
                ItemPedido(
                    nomeProduto = cursor.getString(0),
                    quantidade = cursor.getInt(1),
                    precoUnitario = cursor.getDouble(2),
                    subtotal = cursor.getDouble(3)
                )
            )
        }
        cursor.close()
        return itens
    }

    private fun getCurrentDate(): String {
        val calendar = java.util.Calendar.getInstance()
        return java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(calendar.time)
    }
}