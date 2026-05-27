package com.example.horta.loja

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.components.BottomNavBar
import com.example.horta.ui.components.BaseScreen

@Composable
fun CarrinhoScreen(
    onNavigateTo: (String) -> Unit,
    onFinalizar: () -> Unit,
    onVoltar: () -> Unit
) {
    val currentRoute = "loja"

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = onNavigateTo
            )
        }
    ) { paddingValues ->
        BaseScreen(
            titulo = "Carrinho",
            subtitulo = "Revise seus produtos"
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Produto 1 - R$ 10,00")
                        Text("Produto 2 - R$ 20,00")
                        Text("Produto 3 - R$ 15,00")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Total: R$ 45,00", fontSize = 18.sp, color = Color(0xFF4CAF50))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onFinalizar,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Finalizar Compra", fontSize = 18.sp)
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
}