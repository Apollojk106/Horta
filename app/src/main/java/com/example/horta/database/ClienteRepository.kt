package com.example.horta.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.horta.utilities.DatabaseHelper
import com.example.horta.utilities.SecurityHelper

class ClienteRepository(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper(context).writableDatabase
    private val sessaoRepo = SessaoRepository(context)

    data class Cliente(
        val id: Long,
        val nome: String,
        val email: String,
        val senhaHash: String,
        val telefone: String,
        val cep: String,
        val dataCad: String? = null
    )

    // CADASTRO
    fun cadastrar(nome: String, email: String, senha: String, telefone: String = "", cep: String = ""): Boolean {
        return try {
            val senhaHash = SecurityHelper.hashPassword(senha)
            val values = ContentValues().apply {
                put("nome", nome)
                put("email", email)
                put("senha", senhaHash)
                put("telefone", telefone)
                put("cep", cep)
            }
            val result = db.insert("cliente", null, values)
            result != -1L
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // LOGIN - Retorna cliente e token
    fun login(email: String, senha: String): Pair<Cliente?, String?> {
        val cursor = db.query(
            "cliente",
            arrayOf("id_cliente", "nome", "email", "senha", "telefone", "cep"),
            "email = ?",
            arrayOf(email),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            val hashSalvo = cursor.getString(3)

            if (SecurityHelper.verifyPassword(senha, hashSalvo)) {
                val cliente = Cliente(
                    id = cursor.getLong(0),
                    nome = cursor.getString(1),  // ← Campo nome
                    email = cursor.getString(2),
                    senhaHash = hashSalvo,
                    telefone = cursor.getString(4),
                    cep = cursor.getString(5)
                )
                // Criar sessão para o usuário logado
                val token = sessaoRepo.criarSessao(cliente.id)
                cursor.close()
                Pair(cliente, token)
            } else {
                cursor.close()
                Pair(null, null)
            }
        } else {
            cursor.close()
            Pair(null, null)
        }
    }

    fun getClienteById(idCliente: Long): Cliente? {
        val cursor = db.query(
            "cliente",
            arrayOf("id_cliente", "nome", "email", "senha", "telefone", "cep", "dataCad"),
            "id_cliente = ?",
            arrayOf(idCliente.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            Cliente(
                id = cursor.getLong(0),
                nome = cursor.getString(1),
                email = cursor.getString(2),
                senhaHash = cursor.getString(3),
                telefone = cursor.getString(4),
                cep = cursor.getString(5),
                dataCad = cursor.getString(6)  // Adicione este campo
            )
        } else {
            null
        }.also { cursor.close() }
    }

    fun emailExiste(email: String): Boolean {
        val cursor = db.query(
            "cliente",
            arrayOf("email"),
            "email = ?",
            arrayOf(email),
            null, null, null
        )
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    // Buscar cliente por email (para recuperação de senha)
    fun getClienteByEmail(email: String): Cliente? {
        val cursor = db.query(
            "cliente",
            arrayOf("id_cliente", "nome", "email", "senha", "telefone", "cep"),
            "email = ?",
            arrayOf(email),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            Cliente(
                id = cursor.getLong(0),
                nome = cursor.getString(1),
                email = cursor.getString(2),
                senhaHash = cursor.getString(3),
                telefone = cursor.getString(4),
                cep = cursor.getString(5)
            )
        } else {
            null
        }.also { cursor.close() }
    }

    // Atualizar senha
    fun atualizarSenha(idCliente: Long, novaSenha: String): Boolean {
        val novaSenhaHash = SecurityHelper.hashPassword(novaSenha)
        val values = ContentValues().apply {
            put("senha", novaSenhaHash)
        }
        val rows = db.update("cliente", values, "id_cliente = ?", arrayOf(idCliente.toString()))
        return rows > 0
    }
}