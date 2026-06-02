package com.example.horta.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.horta.utilities.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    fun criarPedido(
        idCliente: Long,
        itens: List<Pair<Long, Int>>,
        total: Double,
        metodoPagamento: String,
        troco: Double = 0.0
    ): Long {
        return try {
            db.beginTransaction()

            val idCursor = db.rawQuery("SELECT COALESCE(MAX(id_pedido), 0) + 1 FROM pedido", null)
            idCursor.moveToFirst()
            val idPedido = idCursor.getLong(0)
            idCursor.close()

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, 5)
            val dataEntrega = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

            val pedidoValues = ContentValues().apply {
                put("id_pedido", idPedido)
                put("dataEntrega", dataEntrega)
                put("id_cliente", idCliente)
            }
            db.insert("pedido", null, pedidoValues)

            // Inserir itens do pedido
            for (item in itens) {
                val produtoId = item.first
                val quantidade = item.second

                // Buscar preço do produto
                val precoCursor = db.query(
                    "produto",
                    arrayOf("preco"),
                    "id_produto = ?",
                    arrayOf(produtoId.toString()),
                    null, null, null
                )
                val preco = if (precoCursor.moveToFirst()) precoCursor.getDouble(0) else 0.0
                precoCursor.close()

                val itemValues = ContentValues().apply {
                    put("quantidade", quantidade)
                    put("id_pedido", idPedido)
                    put("id_produto", produtoId)
                    put("preco", preco)
                }
                db.insert("pedido_produto", null, itemValues)
            }

            db.setTransactionSuccessful()
            db.endTransaction()
            idPedido
        } catch (e: Exception) {
            db.endTransaction()
            e.printStackTrace()
            0L
        }
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