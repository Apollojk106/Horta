package com.example.horta.ui.theme

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
fun DoacaoScreen(
    onNavigateTo: (String) -> Unit,
    onVoltar: (String) -> Unit,
) {
    val currentRoute = "doacao"  // Indica que está na tela Doação

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    when (route) {
                        "inicio" -> onNavigateTo("inicio")
                        "horta" -> onNavigateTo("horta")
                        "loja" -> onNavigateTo("loja")
                        "doacao" -> { /* Já está na tela de doação */ }
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
            // 🔍 TEXTO PARA IDENTIFICAR A TELA (remova depois)
            Text(
                text = "📍 TELA: DOAÇÃO",
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text("🎁", fontSize = 64.sp)
            Text("Doação", fontSize = 32.sp, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Ajude a comunidade fazendo uma doação", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onNavigateTo("loja") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar para Loja")
            }
        }
    }
}