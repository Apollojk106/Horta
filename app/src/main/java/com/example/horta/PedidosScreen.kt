package com.example.horta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.components.BottomNavBar

data class Pedido(
    val id: Int,
    val data: String,
    val total: String,
    val status: String,
    val items: List<String>
)

@Composable
fun PedidosScreen(
    onNavigateTo: (String) -> Unit
) {
    val currentRoute = "pedidos"

    val pedidos = listOf(
        Pedido(
            id = 1,
            data = "15/03/2024",
            total = "R$ 45,00",
            status = "Entregue",
            items = listOf("Alface", "Tomate", "Cebolinha")
        ),
        Pedido(
            id = 2,
            data = "10/03/2024",
            total = "R$ 32,50",
            status = "Entregue",
            items = listOf("Mudas de Hortelã", "Adubo Orgânico")
        ),
        Pedido(
            id = 3,
            data = "05/03/2024",
            total = "R$ 78,90",
            status = "Cancelado",
            items = listOf("Vaso Autoirrigável", "Terra Adubada", "Sementes")
        ),
        Pedido(
            id = 4,
            data = "28/02/2024",
            total = "R$ 120,00",
            status = "Entregue",
            items = listOf("Kit Horta Completo", "Fertilizante")
        )
    )

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("📍 TELA: PEDIDOS", fontSize = 12.sp, color = Color.Red)

            Text("📋", fontSize = 48.sp)
            Text("Meus Pedidos", fontSize = 28.sp, color = Color(0xFF4CAF50))
            Text("Histórico de compras", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            if (pedidos.isEmpty()) {
                // Caso não tenha pedidos
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("🛒", fontSize = 64.sp)
                    Text("Nenhum pedido encontrado", fontSize = 18.sp, color = Color.Gray)
                    Text("Faça sua primeira compra!", fontSize = 14.sp, color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pedidos) { pedido ->
                        PedidoCard(pedido = pedido)
                    }
                }
            }
        }
    }
}

@Composable
fun PedidoCard(pedido: Pedido) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (pedido.status == "Entregue") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pedido #${pedido.id}",
                    fontSize = 16.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Text(
                    text = pedido.data,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            pedido.items.forEach { item ->
                Text("• $item", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total: ${pedido.total}",
                    fontSize = 16.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )

            }
        }
    }
}