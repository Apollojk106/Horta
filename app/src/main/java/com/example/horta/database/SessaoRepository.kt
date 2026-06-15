package com.example.horta.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.horta.utilities.DatabaseHelper
import java.util.UUID
import java.util.Calendar

class SessaoRepository(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper(context).writableDatabase

    data class Sessao(
        val idSessao: Long,
        val idCliente: Long,
        val tokenSessao: String,
        val dataLogin: String,
        val dataExpiracao: String,
        val ativo: Boolean
    )

    fun criarSessao(idCliente: Long): String {
        val token = UUID.randomUUID().toString()
        val calendar = Calendar.getInstance()
        val dataAtual = getCurrentDateTime()

        calendar.add(Calendar.DAY_OF_MONTH, 7)
        val dataExpiracao = formatDateTime(calendar)

        val values = ContentValues().apply {
            put("id_cliente", idCliente)
            put("token_sessao", token)
            put("data_login", dataAtual)
            put("data_expiracao", dataExpiracao)
            put("ativo", 1)
        }

        db.insert("sessao", null, values)
        return token
    }

    private fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        return formatDateTime(calendar)
    }

    private fun formatDateTime(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second)
    }

    fun getUltimaSessaoAtiva(): Sessao? {
        val cursor = db.rawQuery("""
        SELECT id_sessao, id_cliente, token_sessao, data_login, data_expiracao, ativo
        FROM sessao 
        WHERE data_expiracao >= datetime('now')
        ORDER BY id_sessao DESC 
        LIMIT 1
    """, null)

        return if (cursor.moveToFirst()) {
            Sessao(
                idSessao = cursor.getLong(0),
                idCliente = cursor.getLong(1),
                tokenSessao = cursor.getString(2),
                dataLogin = cursor.getString(3),
                dataExpiracao = cursor.getString(4),
                ativo = cursor.getInt(5) == 1
            )
        } else {
            null
        }.also { cursor.close() }
    }
}