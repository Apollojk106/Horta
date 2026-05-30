package com.example.horta.model

data class ItemCarrinho(
    val idProduto: Long,
    val nome: String,
    val quantidade: Int,
    val precoUnitario: Double
) {
    val subtotal: Double
        get() = quantidade * precoUnitario
}

class Carrinho {
    private val itens = mutableListOf<ItemCarrinho>()

    fun adicionarItem(produtoId: Long, nome: String, preco: Double, quantidade: Int = 1) {
        val itemExistente = itens.find { it.idProduto == produtoId }

        if (itemExistente != null) {
            val index = itens.indexOf(itemExistente)
            itens[index] = itemExistente.copy(quantidade = itemExistente.quantidade + quantidade)
        } else {
            itens.add(ItemCarrinho(produtoId, nome, quantidade, preco))
        }
    }

    fun removerItem(produtoId: Long) {
        itens.removeAll { it.idProduto == produtoId }
    }

    fun atualizarQuantidade(produtoId: Long, quantidade: Int) {
        val index = itens.indexOfFirst { it.idProduto == produtoId }
        if (index != -1) {
            val item = itens[index]
            if (quantidade <= 0) {
                itens.removeAt(index)
            } else {
                itens[index] = item.copy(quantidade = quantidade)
            }
        }
    }

    fun limpar() {
        itens.clear()
    }

    fun getItens(): List<ItemCarrinho> = itens.toList()

    fun getTotal(): Double = itens.sumOf { it.subtotal }

    fun getQuantidadeTotal(): Int = itens.sumOf { it.quantidade }

    fun isEmpty(): Boolean = itens.isEmpty()
}