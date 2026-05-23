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
fun LojaScreen(
    onNavigateTo: (String) -> Unit,  // Navegação entre telas
    onVerCarrinho: () -> Unit,
    onLogout: () -> Unit,
    onVerHorta: () -> Unit,
    onVerDoacao: () -> Unit,

) {
    val currentRoute = "loja"  // Indica que está na tela Loja

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
                text = "📍 TELA: LOJA",
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text("🛒", fontSize = 64.sp)
            Text("Loja", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onVerCarrinho, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Carrinho")
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