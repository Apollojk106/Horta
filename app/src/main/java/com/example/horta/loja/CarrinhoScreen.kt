package com.example.horta.loja

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.R
import com.example.horta.components.BottomNavBar
import com.example.horta.database.CarrinhoRepository
import com.example.horta.ui.components.LojaBaseScreen
import com.example.horta.ui.components.TopBarCompleta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CarrinhoScreen(
    currentRoute: String = "loja",
    onNavigateTo: (String) -> Unit,
    onFinalizar: () -> Unit,
    onVoltar: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val carrinhoRepository = remember { CarrinhoRepository(context) }

    var itensCarrinho by remember { mutableStateOf<List<CarrinhoRepository.ItemCarrinho>>(emptyList()) }
    var totalCarrinho by remember { mutableStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }
    var mostrarDialogLimpar by remember { mutableStateOf(false) }

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

    fun limparCarrinho() {
        scope.launch {
            withContext(Dispatchers.IO) {
                carrinhoRepository.limparCarrinho()
                carregarCarrinho()
            }
            mostrarDialogLimpar = false
        }
    }

    fun aumentarQuantidade(item: CarrinhoRepository.ItemCarrinho) {
        scope.launch {
            withContext(Dispatchers.IO) {
                carrinhoRepository.adicionarItem(item.idProduto, item.nomeProduto, item.preco)
                carregarCarrinho()
            }
        }
    }

    fun diminuirQuantidade(item: CarrinhoRepository.ItemCarrinho) {
        scope.launch {
            withContext(Dispatchers.IO) {
                if (item.quantidade <= 1) {
                    carrinhoRepository.removerItem(item.idProduto)
                } else {
                    carrinhoRepository.diminuirItem(item.idProduto)
                }
                carregarCarrinho()
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

        LojaBaseScreen(
            onCarrinhoClick = {}
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
                } else if (itensCarrinho.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lojaicon),
                            contentDescription = "Carrinho vazio",
                            modifier = Modifier.size(80.dp)
                        )
                        Text(
                            text = "Seu carrinho está vazio",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onVoltar,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("Continuar comprando")
                        }
                    }
                } else {
                    TopBarCompleta(
                        titulo = "Carrinho",
                        onVoltarClick = onVoltar,
                        onLimparClick = { mostrarDialogLimpar = true }
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itensCarrinho.forEach { item ->
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
                                                text = item.nomeProduto,
                                                fontSize = 16.sp,
                                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                            Text(
                                                text = "Unitário: R$ ${String.format("%.2f", item.preco)}",
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            )
                                        }
                                        Text(
                                            text = "R$ ${String.format("%.2f", item.preco * item.quantidade)}",
                                            fontSize = 16.sp,
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
                                            onClick = { diminuirQuantidade(item) },
                                            modifier = Modifier.size(36.dp),
                                            enabled = item.quantidade > 0
                                        ) {
                                            Text("➖", fontSize = 20.sp)
                                        }

                                        Text(
                                            text = "${item.quantidade}",
                                            fontSize = 18.sp,
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 12.dp)
                                        )

                                        IconButton(
                                            onClick = { aumentarQuantidade(item) },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Text("➕", fontSize = 20.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total:",
                                fontSize = 18.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                text = "R$ ${String.format("%.2f", totalCarrinho)}",
                                fontSize = 18.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onFinalizar,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Finalizar Compra", fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onVoltar,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Voltar à Loja", fontSize = 16.sp)
                    }
                }
            }
        }
    }

    // Dialog de confirmação para limpar o carrinho
    if (mostrarDialogLimpar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogLimpar = false },
            title = { Text("Limpar Carrinho", color = Color(0xFFF44336)) },
            text = { Text("Tem certeza que deseja remover todos os itens do carrinho?") },
            confirmButton = {
                Button(
                    onClick = { limparCarrinho() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text("Sim, limpar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogLimpar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}