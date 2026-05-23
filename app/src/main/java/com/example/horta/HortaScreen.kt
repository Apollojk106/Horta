package com.example.horta.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.components.BottomNavBar

@Composable
fun HortaScreen(
    onNavigateTo: (String) -> Unit,
    onLogout: () -> Unit
) {
    // A rota atual é "horta" - o componente vai destacar o ícone da horta
    val currentRoute = "horta"

    Text(
        text = "📍 TELA: Horta",
        fontSize = 12.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    when (route) {
                        "inicio" -> onNavigateTo("inicio")
                        "horta" -> { /* Já está na horta */ }
                        "loja" -> onNavigateTo("loja")
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🌱", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Horta", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Aqui você acompanha sua horta")
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { onNavigateTo("loja") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir para Loja")
            }
        }
    }
}