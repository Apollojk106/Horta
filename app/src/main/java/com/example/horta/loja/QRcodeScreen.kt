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
fun QRcodeScreen(
    onNavigateTo: (String) -> Unit,
    onConcluir: () -> Unit
) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("📍 TELA: QR CODE", fontSize = 12.sp, color = Color.Red)
            Text("📱", fontSize = 80.sp)
            Text("QR Code", fontSize = 32.sp, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Escaneie o código para pagamento", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(32.dp))

            // Simulação do QR Code
            Card(
                modifier = Modifier.size(200.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("QR Code", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onConcluir,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Concluir Compra", fontSize = 18.sp)
            }
        }
    }
}