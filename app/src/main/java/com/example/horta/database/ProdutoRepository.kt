package com.example.horta.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.horta.utilities.DatabaseHelper

class ProdutoRepository(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper(context).readableDatabase

    data class Produto(
        val id: Long,
        val nome: String,
        val preco: Double,
        val quantidade: Int,
        val dataColheita: String,
        val tipo: String = ""
    )

    fun getAllProdutos(): List<Produto> {
        val produtos = mutableListOf<Produto>()
        val query = """
            SELECT p.id_produto, p.nome, p.preco, p.quantidade, p.data_colheita, 
                   COALESCE(t.nome_tipo, '') as tipo
            FROM produto p
            LEFT JOIN tipo_produto t ON p.id_tipo = t.id_tipo
            ORDER BY p.nome
        """
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            produtos.add(
                Produto(
                    id = cursor.getLong(0),
                    nome = cursor.getString(1),
                    preco = cursor.getDouble(2),
                    quantidade = cursor.getInt(3),
                    dataColheita = cursor.getString(4),
                    tipo = cursor.getString(5)
                )
            )
        }
        cursor.close()
        return produtos
    }
}