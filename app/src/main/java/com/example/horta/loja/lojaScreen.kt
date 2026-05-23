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
import com.example.horta.database.ProdutoRepository
import kotlinx.coroutines.*

@Composable
fun LojaScreen(
    onNavigateTo: (String) -> Unit,
    onVerCarrinho: () -> Unit,
    onLogout: () -> Unit,
    onVerHorta: () -> Unit,
    onVerDoacao: () -> Unit,
) {
    val currentRoute = "loja"
    val context = LocalContext.current
    val repository = remember { ProdutoRepository(context) }

    var produtos by remember { mutableStateOf<List<ProdutoRepository.Produto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Carregar produtos do banco de dados
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val lista = repository.getAllProdutos()
                withContext(Dispatchers.Main) {
                    produtos = lista
                    isLoading = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = e.message
                    isLoading = false
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    when (route) {
                        "inicio" -> onNavigateTo("inicio")
                        "horta" -> onNavigateTo("horta")
                        "loja" -> { /* Já está na loja */ }
                        "doacao" -> onNavigateTo("doacao")
                        "pedidos" -> onNavigateTo("pedidos")
                        "perfil" -> onNavigateTo("perfil")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Título
            Text("🛒", fontSize = 48.sp)
            Text("Nossos Produtos", fontSize = 28.sp, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(16.dp))

            // Conteúdo principal
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                        Text("Carregando produtos...")
                    }
                }
                errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("❌ Erro: $errorMessage", color = Color.Red)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { /* Recarregar */ }) {
                                Text("Tentar novamente")
                            }
                        }
                    }
                }
                produtos.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nenhum produto encontrado", fontSize = 18.sp, color = Color.Gray)
                    }
                }
                else -> {
                    // Lista de produtos
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(produtos) { produto ->
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
                                    Column {
                                        Text(
                                            text = produto.nome,
                                            fontSize = 18.sp,
                                            color = Color(0xFF2E7D32)
                                        )
                                        Text(
                                            text = "Estoque: ${produto.quantidade} unidades",
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
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botões
                    Button(
                        onClick = onVerCarrinho,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Ver Carrinho", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { onNavigateTo("horta") }, modifier = Modifier.fillMaxWidth()) {
                        Text("Horta")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { onNavigateTo("doacao") }, modifier = Modifier.fillMaxWidth()) {
                        Text("Doação")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                    ) {
                        Text("Sair")
                    }
                }
            }
        }
    }
}