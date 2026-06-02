package com.example.horta

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
import com.example.horta.database.PedidoRepository
import com.example.horta.database.SessaoRepository
import com.example.horta.ui.components.BaseScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PedidosScreen(
    onNavigateTo: (String) -> Unit
) {
    val currentRoute = "pedidos"
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pedidoRepo = remember { PedidoRepository(context) }

    var pedidos by remember { mutableStateOf<List<PedidoRepository.Pedido>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val sessaoRepo = SessaoRepository(context)
                    val sessao = sessaoRepo.getUltimaSessaoAtiva()
                    if (sessao != null) {
                        val listaPedidos = pedidoRepo.getPedidosByCliente(sessao.idCliente)
                        withContext(Dispatchers.Main) {
                            pedidos = listaPedidos
                            isLoading = false
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            isLoading = false
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        isLoading = false
                    }
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
                        "home" -> onNavigateTo("home")
                        "horta" -> onNavigateTo("horta")
                        "loja" -> onNavigateTo("loja")
                        "pedidos" -> { /* Já está */ }
                        "perfil" -> onNavigateTo("perfil")
                    }
                }
            )
        }
    ) { paddingValues ->
        BaseScreen(
            titulo = "MEUS PEDIDOS",
            subtitulo = "Acompanhe suas compras"
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
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
                    pedidos.isEmpty() -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "📋",
                                fontSize = 64.sp
                            )
                            Text(
                                text = "Nenhum pedido encontrado",
                                fontSize = 18.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Faça sua primeira compra!",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    else -> {
                        pedidos.forEach { pedido ->
                            PedidoCard(
                                pedido = pedido,
                                pedidoRepo = pedidoRepo
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PedidoCard(
    pedido: PedidoRepository.Pedido,
    pedidoRepo: PedidoRepository
) {
    var expanded by remember { mutableStateOf(false) }
    var itensPedido by remember { mutableStateOf<List<PedidoRepository.ItemPedido>>(emptyList()) }
    var isLoadingItens by remember { mutableStateOf(false) }

    LaunchedEffect(expanded) {
        if (expanded && itensPedido.isEmpty()) {
            isLoadingItens = true
            val itens = pedidoRepo.getItensDoPedido(pedido.id)
            itensPedido = itens
            isLoadingItens = false
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (pedido.status == "Entregue") Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pedido #${pedido.id}",
                        fontSize = 16.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = pedido.dataPedido,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Surface(
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                    color = if (pedido.status == "Entregue")
                        Color(0xFF4CAF50).copy(alpha = 0.2f)
                    else
                        Color(0xFFFF9800).copy(alpha = 0.2f)
                ) {
                    Text(
                        text = pedido.status,
                        fontSize = 12.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                        color = if (pedido.status == "Entregue") Color(0xFF4CAF50) else Color(0xFFFF9800),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "R$ ${String.format("%.2f", pedido.total)}",
                    fontSize = 16.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Previsão de entrega:",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = pedido.dataEntrega,
                    fontSize = 12.sp,
                    color = Color(0xFF2E7D32)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (expanded) "▲ Ver menos" else "▼ Ver itens",
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50)
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))

                if (isLoadingItens) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                } else if (itensPedido.isEmpty()) {
                    Text(
                        text = "Nenhum item encontrado",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                } else {
                    itensPedido.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.nomeProduto} x${item.quantidade}",
                                fontSize = 13.sp,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                text = "R$ ${String.format("%.2f", item.subtotal)}",
                                fontSize = 13.sp,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }
        }
    }
}