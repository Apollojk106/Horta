package com.example.horta

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
fun PerfilScreen(
    onNavigateTo: (String) -> Unit,
    onLogout: () -> Unit
) {
    val currentRoute = "perfil"

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
                        "perfil" -> { /* Já está no perfil */ }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🔍 TEXTO PARA IDENTIFICAR A TELA (remova depois)
            Text(
                text = "📍 TELA: PERFIL",
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text("👤", fontSize = 80.sp)
            Text("Perfil", fontSize = 32.sp, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📧 Email: usuario@horta.com")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("👤 Nome: Usuário da Horta")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("🌱 Membro desde: 2024")
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sair", fontSize = 18.sp)
            }
        }
    }
}