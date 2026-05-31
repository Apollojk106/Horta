package com.example.horta.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.horta.utilities.DatabaseHelper

class CarrinhoRepository(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper(context).writableDatabase

    data class ItemCarrinho(
        val idCarrinho: Long,
        val idProduto: Long,
        val nomeProduto: String,
        val preco: Double,
        val quantidade: Int
    )

    // Adicionar item ao carrinho (ou aumentar quantidade se já existir)
    fun adicionarItem(idProduto: Long, nomeProduto: String, preco: Double): Boolean {
        val itemExistente = buscarItemPorProduto(idProduto)

        return if (itemExistente != null) {
            // Atualizar quantidade
            val novaQuantidade = itemExistente.quantidade + 1
            atualizarQuantidade(itemExistente.idCarrinho, novaQuantidade)
        } else {
            // Inserir novo item
            val values = ContentValues().apply {
                put("id_produto", idProduto)
                put("nome_produto", nomeProduto)
                put("quantidade", 1)
            }
            val result = db.insert("carrinho", null, values)
            result != -1L
        }
    }

    // Remover item do carrinho
    fun removerItem(idCarrinho: Long): Boolean {
        val rows = db.delete("carrinho", "id_carrinho = ?", arrayOf(idCarrinho.toString()))
        return rows > 0
    }

    // Diminuir quantidade de um item
    fun diminuirQuantidade(idCarrinho: Long): Boolean {
        val item = buscarItemPorId(idCarrinho)
        return if (item != null) {
            if (item.quantidade <= 1) {
                removerItem(idCarrinho)
            } else {
                atualizarQuantidade(idCarrinho, item.quantidade - 1)
            }
        } else {
            false
        }
    }

    // Atualizar quantidade
    fun atualizarQuantidade(idCarrinho: Long, novaQuantidade: Int): Boolean {
        val values = ContentValues().apply {
            put("quantidade", novaQuantidade)
        }
        val rows = db.update("carrinho", values, "id_carrinho = ?", arrayOf(idCarrinho.toString()))
        return rows > 0
    }

    // Buscar item por ID do produto
    fun buscarItemPorProduto(idProduto: Long): ItemCarrinho? {
        val cursor = db.query(
            "carrinho",
            arrayOf("id_carrinho", "id_produto", "nome_produto", "quantidade"),
            "id_produto = ?",
            arrayOf(idProduto.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            ItemCarrinho(
                idCarrinho = cursor.getLong(0),
                idProduto = cursor.getLong(1),
                nomeProduto = cursor.getString(2),
                preco = 0.0, // Será preenchido depois com o preço do produto
                quantidade = cursor.getInt(3)
            )
        } else {
            null
        }.also { cursor.close() }
    }

    // Buscar item por ID do carrinho
    fun buscarItemPorId(idCarrinho: Long): ItemCarrinho? {
        val cursor = db.query(
            "carrinho",
            arrayOf("id_carrinho", "id_produto", "nome_produto", "quantidade"),
            "id_carrinho = ?",
            arrayOf(idCarrinho.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            ItemCarrinho(
                idCarrinho = cursor.getLong(0),
                idProduto = cursor.getLong(1),
                nomeProduto = cursor.getString(2),
                preco = 0.0,
                quantidade = cursor.getInt(3)
            )
        } else {
            null
        }.also { cursor.close() }
    }

    // Buscar todos os itens do carrinho com preço
    fun buscarTodosItens(): List<ItemCarrinho> {
        val itens = mutableListOf<ItemCarrinho>()
        val query = """
            SELECT c.id_carrinho, c.id_produto, c.nome_produto, c.quantidade, p.preco
            FROM carrinho c
            JOIN produto p ON c.id_produto = p.id_produto
        """
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            itens.add(
                ItemCarrinho(
                    idCarrinho = cursor.getLong(0),
                    idProduto = cursor.getLong(1),
                    nomeProduto = cursor.getString(2),
                    quantidade = cursor.getInt(3),
                    preco = cursor.getDouble(4)
                )
            )
        }
        cursor.close()
        return itens
    }

    // Calcular total do carrinho
    fun calcularTotal(): Double {
        var total = 0.0
        val query = """
            SELECT c.quantidade, p.preco
            FROM carrinho c
            JOIN produto p ON c.id_produto = p.id_produto
        """
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            total += cursor.getInt(0) * cursor.getDouble(1)
        }
        cursor.close()
        return total
    }

    // Limpar carrinho
    fun limparCarrinho(): Boolean {
        val rows = db.delete("carrinho", null, null)
        return rows > 0
    }

    fun diminuirItem(idProduto: Long): Boolean {
        val cursor = db.query(
            "carrinho",
            arrayOf("id_carrinho", "quantidade"),
            "id_produto = ?",
            arrayOf(idProduto.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            val idCarrinho = cursor.getLong(0)
            val quantidade = cursor.getInt(1)
            cursor.close()

            if (quantidade <= 1) {
                removerItem(idProduto)
            } else {
                atualizarQuantidade(idCarrinho, quantidade - 1)
            }
        } else {
            cursor.close()
            false
        }
    }
}