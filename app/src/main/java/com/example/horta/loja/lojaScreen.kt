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
import com.example.horta.database.ProdutoRepository
import com.example.horta.ui.components.BaseScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LojaScreen(
    currentRoute: String = "loja",
    onNavigateTo: (String) -> Unit,
    onVerCarrinho: () -> Unit,
    onLogout: () -> Unit
) {

    val context = LocalContext.current
    val repository = remember { ProdutoRepository(context) }

    var produtos by remember {
        mutableStateOf<List<ProdutoRepository.Produto>>(emptyList())
    }

    var isLoading by remember { mutableStateOf(true) }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    // CARREGAR PRODUTOS
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
                onItemSelected = onNavigateTo
            )
        }

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

                when {

                    // LOADING
                    isLoading -> {

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {

                            CircularProgressIndicator()

                        }
                    }

                    // ERRO
                    errorMessage != null -> {

                        Text(
                            text = "Erro: $errorMessage",
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                    }

                    // SEM PRODUTOS
                    produtos.isEmpty() -> {

                        Text(
                            text = "Nenhum produto encontrado",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }

                    // LISTA DE PRODUTOS
                    else -> {

                        produtos.forEach { produto ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),

                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE8F5E9)
                                )
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),

                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Column {

                                        Text(
                                            text = produto.nome,
                                            fontSize = 18.sp,
                                            color = Color(0xFF2E7D32)
                                        )

                                        Spacer(
                                            modifier = Modifier.height(4.dp)
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
                                        text = "R$ ${
                                            String.format(
                                                "%.2f",
                                                produto.preco
                                            )
                                        }",

                                        fontSize = 20.sp,
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // BOTÃO CARRINHO
                        Button(
                            onClick = onVerCarrinho,

                            modifier = Modifier.fillMaxWidth(),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            )
                        ) {

                            Text(
                                text = "Ver Carrinho",
                                fontSize = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // BOTÃO SAIR
                        Button(
                            onClick = onLogout,

                            modifier = Modifier.fillMaxWidth(),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF44336)
                            )
                        ) {

                            Text(
                                text = "Sair",
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}