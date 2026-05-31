package com.example.horta.loja

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.components.BottomNavBar
import com.example.horta.database.CarrinhoRepository
import com.example.horta.database.ProdutoRepository
import com.example.horta.ui.components.BaseScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LojaScreen(
    currentRoute: String = "loja",
    onNavigateTo: (String) -> Unit,
    onVerCarrinho: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val produtoRepository = remember { ProdutoRepository(context) }
    val carrinhoRepository = remember { CarrinhoRepository(context) }

    var produtos by remember { mutableStateOf<List<ProdutoRepository.Produto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    var quantidades by remember { mutableStateOf(mutableMapOf<Long, Int>()) }
    var totalCarrinhoAtual by remember { mutableStateOf(0.0) }
    var totalPreview by remember { mutableStateOf(0.0) }
    var mostrarPopup by remember { mutableStateOf(false) }
    var totalCarrinhoTemp by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val lista = produtoRepository.getAllProdutos()
            val total = carrinhoRepository.calcularTotal()
            withContext(Dispatchers.Main) {
                produtos = lista
                quantidades = lista.associate { it.id to 0 }.toMutableMap()
                totalCarrinhoAtual = total
                totalPreview = total
                isLoading = false
            }
        }
    }

    fun calcularTotalSelecionado(): Double {
        var total = 0.0
        produtos.forEach { produto ->
            val qtd = quantidades[produto.id] ?: 0
            total += qtd * produto.preco
        }
        return total
    }

    fun aumentarQuantidade(produto: ProdutoRepository.Produto) {
        val qtdAtual = quantidades[produto.id] ?: 0
        if (qtdAtual < produto.quantidade) {
            quantidades = quantidades.toMutableMap().apply {
                this[produto.id] = qtdAtual + 1
            }
            totalPreview = totalCarrinhoAtual + calcularTotalSelecionado()
        }
    }

    fun diminuirQuantidade(produto: ProdutoRepository.Produto) {
        val qtdAtual = quantidades[produto.id] ?: 0
        if (qtdAtual > 0) {
            quantidades = quantidades.toMutableMap().apply {
                this[produto.id] = qtdAtual - 1
            }
            totalPreview = totalCarrinhoAtual + calcularTotalSelecionado()
        }
    }

    fun adicionarAoCarrinho() {
        val itensParaAdicionar = produtos.filter {
            (quantidades[it.id] ?: 0) > 0
        }
        if (itensParaAdicionar.isEmpty()) {
            onVerCarrinho()
            return
        }
        totalCarrinhoTemp = totalCarrinhoAtual + calcularTotalSelecionado()
        mostrarPopup = true
    }

    fun confirmarAdicao() {
        scope.launch {
            withContext(Dispatchers.IO) {
                produtos.forEach { produto ->
                    val qtd = quantidades[produto.id] ?: 0
                    if (qtd > 0) {
                        for (i in 1..qtd) {
                            carrinhoRepository.adicionarItem(produto.id, produto.nome, produto.preco)
                        }
                    }
                }
            }
            quantidades = quantidades.mapValues { 0 }.toMutableMap()
            totalCarrinhoAtual = totalCarrinhoTemp
            totalPreview = totalCarrinhoTemp
            mostrarPopup = false
            onVerCarrinho()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = onNavigateTo
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { adicionarAoCarrinho() },
                containerColor = Color(0xFF4CAF50),
                modifier = Modifier
                    .size(width = 200.dp, height = 56.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ver carrinho", fontSize = 20.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "R$ ${String.format("%.2f", totalPreview)}",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->

        BaseScreen(
            titulo = "LOJA",
            subtitulo = "Produtos disponíveis"
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    produtos.forEach { produto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F5E9)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = produto.nome,
                                            fontSize = 18.sp,
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                            color = Color(0xFF2E7D32)
                                        )
                                        Text(
                                            text = "Estoque: ${produto.quantidade}",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = "Colheita: ${produto.dataColheita}",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Text(
                                        text = "R$ ${String.format("%.2f", produto.preco)}",
                                        fontSize = 20.sp,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        color = Color(0xFF4CAF50)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { diminuirQuantidade(produto) },
                                        modifier = Modifier.size(36.dp),
                                        enabled = (quantidades[produto.id] ?: 0) > 0
                                    ) {
                                        Text("➖", fontSize = 20.sp)
                                    }

                                    Text(
                                        text = "${quantidades[produto.id] ?: 0}",
                                        fontSize = 18.sp,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )

                                    IconButton(
                                        onClick = { aumentarQuantidade(produto) },
                                        modifier = Modifier.size(36.dp),
                                        enabled = (quantidades[produto.id] ?: 0) < produto.quantidade
                                    ) {
                                        Text("➕", fontSize = 20.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarPopup) {
        AlertDialog(
            onDismissRequest = { mostrarPopup = false },
            title = { Text("Confirmação", color = Color(0xFF2E7D32)) },
            text = {
                Column {
                    Text("Deseja adicionar ao carrinho?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Novos itens: R$ ${String.format("%.2f", calcularTotalSelecionado())}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Novo total: R$ ${String.format("%.2f", totalCarrinhoTemp)}",
                        fontSize = 18.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { confirmarAdicao() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarPopup = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}