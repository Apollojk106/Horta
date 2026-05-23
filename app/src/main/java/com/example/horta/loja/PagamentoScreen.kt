package com.example.horta.loja

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.components.BottomNavBar

@Composable
fun PagamentoScreen(
    onNavigateTo: (String) -> Unit,
    onConfirmar: () -> Unit,
    onVoltar: () -> Unit
) {
    var selectedPayment by remember { mutableStateOf("credito") }
    val currentRoute = "loja"

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    when (route) {
                        "inicio" -> onNavigateTo("inicio")
                        "horta" -> onNavigateTo("horta")
                        "loja" -> onNavigateTo("loja")
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("📍 TELA: PAGAMENTO", fontSize = 12.sp, color = Color.Red)
            Text("💳", fontSize = 64.sp)
            Text("Pagamento", fontSize = 32.sp, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total a pagar: R$ 45,00", fontSize = 18.sp, color = Color(0xFF4CAF50))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Selecione a forma de pagamento:", fontSize = 16.sp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedPayment == "credito",
                    onClick = { selectedPayment = "credito" }
                )
                Text("Cartão de Crédito")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedPayment == "debito",
                    onClick = { selectedPayment = "debito" }
                )
                Text("Cartão de Débito")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedPayment == "pix",
                    onClick = { selectedPayment = "pix" }
                )
                Text("PIX")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedPayment == "boleto",
                    onClick = { selectedPayment = "boleto" }
                )
                Text("Boleto")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onConfirmar,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Confirmar Pagamento", fontSize = 18.sp)
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