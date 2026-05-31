package com.example.horta.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.horta.utilities.DatabaseHelper

class EnderecoRepository(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper(context).writableDatabase

    data class Endereco(
        val id: Long,
        val idCliente: Long,
        val rua: String,
        val numero: String,
        val bairro: String,
        val cidade: String,
        val estado: String,
        val cep: String,
        val complemento: String
    )

    fun salvarEndereco(
        idCliente: Long,
        rua: String,
        numero: String,
        bairro: String,
        cidade: String,
        estado: String,
        cep: String,
        complemento: String
    ): Boolean {
        return try {
            val existing = getEnderecoByClienteId(idCliente)

            if (existing != null) {
                val values = ContentValues().apply {
                    put("rua", rua)
                    put("numero", numero)
                    put("bairro", bairro)
                    put("cidade", cidade)
                    put("estado", estado)
                    put("cep", cep)
                    put("complemento", complemento)
                }
                val rows = db.update("endereco", values, "id_cliente = ?", arrayOf(idCliente.toString()))
                rows > 0
            } else {
                val values = ContentValues().apply {
                    put("id_cliente", idCliente)
                    put("rua", rua)
                    put("numero", numero)
                    put("bairro", bairro)
                    put("cidade", cidade)
                    put("estado", estado)
                    put("cep", cep)
                    put("complemento", complemento)
                }
                val result = db.insert("endereco", null, values)
                result != -1L
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getEnderecoByClienteId(idCliente: Long): Endereco? {
        val cursor = db.query(
            "endereco",
            null,
            "id_cliente = ?",
            arrayOf(idCliente.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            Endereco(
                id = cursor.getLong(cursor.getColumnIndexOrThrow("id_endereco")),
                idCliente = cursor.getLong(cursor.getColumnIndexOrThrow("id_cliente")),
                rua = cursor.getString(cursor.getColumnIndexOrThrow("rua")) ?: "",
                numero = cursor.getString(cursor.getColumnIndexOrThrow("numero")) ?: "",
                bairro = cursor.getString(cursor.getColumnIndexOrThrow("bairro")) ?: "",
                cidade = cursor.getString(cursor.getColumnIndexOrThrow("cidade")) ?: "",
                estado = cursor.getString(cursor.getColumnIndexOrThrow("estado")) ?: "",
                cep = cursor.getString(cursor.getColumnIndexOrThrow("cep")) ?: "",
                complemento = cursor.getString(cursor.getColumnIndexOrThrow("complemento")) ?: ""
            )
        } else {
            null
        }.also { cursor.close() }
    }
}