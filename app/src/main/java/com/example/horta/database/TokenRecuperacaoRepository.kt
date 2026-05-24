package com.example.horta.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.horta.utilities.DatabaseHelper
import java.util.UUID
import java.util.Calendar

class TokenRecuperacaoRepository(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper(context).writableDatabase

    data class TokenRecuperacao(
        val idToken: Long,
        val idCliente: Long,
        val token: String,
        val dataCriacao: String,
        val dataExpiracao: String,
        val usado: Boolean
    )

    // Criar token para recuperação de senha (válido apenas hoje)
    fun criarToken(idCliente: Long): String {
        val token = UUID.randomUUID().toString().substring(0, 8).uppercase()

        val calendar = Calendar.getInstance()
        val dataCriacao = formatDateTime(calendar)

        // Token expira HOJE às 23:59:59
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val dataExpiracao = formatDateTime(calendar)

        val values = ContentValues().apply {
            put("id_cliente", idCliente)
            put("token", token)
            put("data_criacao", dataCriacao)
            put("data_expiracao", dataExpiracao)
            put("usado", 0)
        }

        db.insert("token_recuperacao", null, values)
        return token
    }

    // Verificar se o token é válido (não expirado e não usado)
    fun isTokenValido(token: String, idCliente: Long): Boolean {
        val cursor = db.query(
            "token_recuperacao",
            arrayOf("usado", "data_expiracao"),
            "token = ? AND id_cliente = ?",
            arrayOf(token, idCliente.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            val usado = cursor.getInt(0) == 1
            val dataExpiracao = cursor.getString(1)
            val isExpirado = dataExpiracao < getCurrentDateTime()
            cursor.close()
            !usado && !isExpirado
        } else {
            cursor.close()
            false
        }
    }

    // Buscar token válido
    fun getTokenRecuperacao(token: String, idCliente: Long): TokenRecuperacao? {
        val cursor = db.query(
            "token_recuperacao",
            null,
            "token = ? AND id_cliente = ? AND usado = 0 AND data_expiracao >= ?",
            arrayOf(token, idCliente.toString(), getCurrentDateTime()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            TokenRecuperacao(
                idToken = cursor.getLong(cursor.getColumnIndexOrThrow("id_token")),
                idCliente = cursor.getLong(cursor.getColumnIndexOrThrow("id_cliente")),
                token = cursor.getString(cursor.getColumnIndexOrThrow("token")),
                dataCriacao = cursor.getString(cursor.getColumnIndexOrThrow("data_criacao")),
                dataExpiracao = cursor.getString(cursor.getColumnIndexOrThrow("data_expiracao")),
                usado = cursor.getInt(cursor.getColumnIndexOrThrow("usado")) == 1
            )
        } else {
            null
        }.also { cursor.close() }
    }

    // Marcar token como usado (após trocar a senha)
    fun marcarTokenComoUsado(idToken: Long): Boolean {
        val values = ContentValues().apply {
            put("usado", 1)
        }
        val rows = db.update("token_recuperacao", values, "id_token = ?", arrayOf(idToken.toString()))
        return rows > 0
    }

    // Limpar tokens expirados (manutenção)
    fun limparTokensExpirados(): Int {
        return db.delete("token_recuperacao", "data_expiracao < ?", arrayOf(getCurrentDateTime()))
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
}