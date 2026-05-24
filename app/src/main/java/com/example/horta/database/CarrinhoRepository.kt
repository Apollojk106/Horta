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

    // Adicionar item ao carrinho
    fun adicionarItem(produtoId: Long, nome: String, preco: Double, quantidade: Int = 1) {
        val itemExistente = itens.find { it.idProduto == produtoId }

        if (itemExistente != null) {
            val index = itens.indexOf(itemExistente)
            itens[index] = itemExistente.copy(quantidade = itemExistente.quantidade + quantidade)
        } else {
            itens.add(ItemCarrinho(produtoId, nome, quantidade, preco))
        }
    }

    // Remover item do carrinho
    fun removerItem(produtoId: Long) {
        itens.removeAll { it.idProduto == produtoId }
    }

    // Atualizar quantidade de um item
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

    // Limpar carrinho
    fun limpar() {
        itens.clear()
    }

    // Obter todos os itens
    fun getItens(): List<ItemCarrinho> = itens.toList()

    // Obter total do carrinho
    fun getTotal(): Double = itens.sumOf { it.subtotal }

    // Obter quantidade total de itens
    fun getQuantidadeTotal(): Int = itens.sumOf { it.quantidade }

    // Verificar se carrinho está vazio
    fun isEmpty(): Boolean = itens.isEmpty()
}