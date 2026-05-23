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
fun EntregaScreen(
    onNavigateTo: (String) -> Unit,
    onProximo: () -> Unit,
    onVoltar: () -> Unit
) {
    var endereco by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
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
            Text("📍 TELA: ENTREGA", fontSize = 12.sp, color = Color.Red)
            Text("📦", fontSize = 64.sp)
            Text("Endereço de Entrega", fontSize = 32.sp, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = endereco,
                onValueChange = { endereco = it },
                label = { Text("Rua") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it },
                label = { Text("Número") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = bairro,
                onValueChange = { bairro = it },
                label = { Text("Bairro") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onProximo,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Próximo", fontSize = 18.sp)
            }

            // ⚠️ SEM BOTÃO VOLTAR
        }
    }
}