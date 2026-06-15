package com.example.horta.loja

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.example.horta.ui.theme.DinamicTypography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CarrinhoScreen(
    currentRoute: String = "loja",
    onNavigateTo: (String) -> Unit,
    onFinalizar: () -> Unit,
    onVoltar: () -> Unit,
    onAtualizarTotal: (Double) -> Unit = {}
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

    LaunchedEffect(totalCarrinho) {
        onAtualizarTotal(totalCarrinho)
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
                            style = DinamicTypography.bodyMedium,
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
                                    .padding(bottom = 12.dp)
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(12.dp),
                                        clip = true
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
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
                                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                                style = DinamicTypography.bodyMedium,
                                                color = Color(0xFF4CAF50)
                                            )
                                            Text(
                                                text = "Unitário: R$ ${String.format("%.2f", item.preco)}",
                                                style = DinamicTypography.bodyMedium,
                                                color = Color.Gray
                                            )
                                        }
                                        Text(
                                            text = "R$ ${String.format("%.2f", item.preco * item.quantidade)}",
                                            style = DinamicTypography.bodyMedium,
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
                                style = DinamicTypography.bodyLarge,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                text = "R$ ${String.format("%.2f", totalCarrinho)}",
                                style = DinamicTypography.bodyLarge,
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.folhaicon),
                                contentDescription = "Folha",
                                modifier = Modifier.size(30.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Finalizar a compra",
                                style = DinamicTypography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onVoltar,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Voltar à Loja", style = DinamicTypography.bodyLarge, color = Color.White)
                    }
                }
            }
        }
    }

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