package com.example.horta.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase

class ProdutoRepository(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper(context).readableDatabase

    data class Produto(
        val id: Long,
        val nome: String,
        val preco: Double,
        val quantidade: Int,
        val dataColheita: String
    )

    fun getAllProdutos(): List<Produto> {
        val produtos = mutableListOf<Produto>()
        val cursor = db.query("produto", null, null, null, null, null, "nome")

        while (cursor.moveToNext()) {
            produtos.add(
                Produto(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow("id_produto")),
                    nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                    preco = cursor.getDouble(cursor.getColumnIndexOrThrow("preco")),
                    quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade")),
                    dataColheita = cursor.getString(cursor.getColumnIndexOrThrow("data_colheita"))
                )
            )
        }
        cursor.close()
        return produtos
    }
}