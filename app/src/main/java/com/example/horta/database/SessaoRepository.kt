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

    // Criar nova sessão após login
    fun criarSessao(idCliente: Long): String {
        val token = UUID.randomUUID().toString()
        val calendar = Calendar.getInstance()
        val dataAtual = getCurrentDateTime()

        // Expira em 7 dias
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

    // Verificar se a sessão é válida
    fun isSessaoValida(token: String): Boolean {
        val cursor = db.query(
            "sessao",
            arrayOf("ativo", "data_expiracao"),
            "token_sessao = ?",
            arrayOf(token),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            val ativo = cursor.getInt(0) == 1
            val dataExpiracao = cursor.getString(1)
            val isExpirada = dataExpiracao < getCurrentDateTime()
            cursor.close()
            ativo && !isExpirada
        } else {
            cursor.close()
            false
        }
    }

    // Buscar sessão ativa por ID do cliente
    fun getSessaoAtivaByCliente(idCliente: Long): Sessao? {
        val cursor = db.query(
            "sessao",
            null,
            "id_cliente = ? AND ativo = 1 AND data_expiracao >= ?",
            arrayOf(idCliente.toString(), getCurrentDateTime()),
            null, null, "data_login DESC",
            "1"
        )

        return if (cursor.moveToFirst()) {
            Sessao(
                idSessao = cursor.getLong(cursor.getColumnIndexOrThrow("id_sessao")),
                idCliente = cursor.getLong(cursor.getColumnIndexOrThrow("id_cliente")),
                tokenSessao = cursor.getString(cursor.getColumnIndexOrThrow("token_sessao")),
                dataLogin = cursor.getString(cursor.getColumnIndexOrThrow("data_login")),
                dataExpiracao = cursor.getString(cursor.getColumnIndexOrThrow("data_expiracao")),
                ativo = cursor.getInt(cursor.getColumnIndexOrThrow("ativo")) == 1
            )
        } else {
            null
        }.also { cursor.close() }
    }

    // Encerrar sessão (logout)
    fun encerrarSessaoPorId(idSessao: Long): Boolean {
        val values = ContentValues().apply {
            put("ativo", 0)
        }
        val rows = db.update("sessao", values, "id_sessao = ?", arrayOf(idSessao.toString()))
        return rows > 0
    }

    // Encerrar todas as sessões de um cliente
    fun encerrarTodasSessoes(idCliente: Long): Boolean {
        val values = ContentValues().apply {
            put("ativo", 0)
        }
        val rows = db.update("sessao", values, "id_cliente = ? AND ativo = 1", arrayOf(idCliente.toString()))
        return rows > 0
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