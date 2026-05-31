package com.example.horta.loja

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.horta.ui.components.BaseScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CarrinhoScreen(
    onNavigateTo: (String) -> Unit,
    onFinalizar: () -> Unit,
    onVoltar: () -> Unit
) {
    val currentRoute = "loja"
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val carrinhoRepository = remember { CarrinhoRepository(context) }

    var itensCarrinho by remember { mutableStateOf<List<CarrinhoRepository.ItemCarrinho>>(emptyList()) }
    var totalCarrinho by remember { mutableStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }

    fun carregarCarrinho() {
        scope.launch {
            withContext(Dispatchers.IO) {
                val itens = carrinhoRepository.buscarTodosItens()
                val total = carrinhoRepository.calcularTotal()
                withContext(Dispatchers.Main) {
                    itensCarrinho = itens
                    totalCarrinho = total
                    isLoading = false
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        carregarCarrinho()
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = onNavigateTo
            )
        }
    ) { paddingValues ->
        BaseScreen(
            titulo = "CARRINHO",
            subtitulo = "Revise seus produtos"
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    itensCarrinho.isEmpty() -> {
                        Text(
                            text = "Carrinho vazio",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                    else -> {
                        // LISTA DE PRODUTOS DO CARRINHO
                        itensCarrinho.forEach { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE8F5E9)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${item.nomeProduto} x${item.quantidade}",
                                        fontSize = 16.sp,
                                        color = Color(0xFF2E7D32)
                                    )
                                    Text(
                                        text = "R$ ${String.format("%.2f", item.preco * item.quantidade)}",
                                        fontSize = 16.sp,
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Total: R$ ${String.format("%.2f", totalCarrinho)}",
                            fontSize = 20.sp,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onFinalizar,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Finalizar Compra", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onVoltar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Voltar")
                }
            }
        }
    }
}