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
import com.example.horta.ui.components.BaseScreen
import com.example.horta.ui.components.LojaBaseScreen
import com.example.horta.ui.components.TopBarCompleta

@Composable
fun EntregaScreen(
    onNavigateTo: (String) -> Unit,
    onProximo: () -> Unit,
    onVoltar: () -> Unit,
    onVerCarrinho: () -> Unit
) {
    var endereco by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    val currentRoute = "loja"

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = onNavigateTo
            )
        }
    ) { paddingValues ->
        LojaBaseScreen(
            onCarrinhoClick = onVerCarrinho
        ) {
            TopBarCompleta(
                titulo = "Endereço",
                onVoltarClick = onVoltar,
                onLimparClick = { }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cidade,
                    onValueChange = { cidade = it },
                    label = { Text("Cidade") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cep,
                    onValueChange = { cep = it },
                    label = { Text("CEP") },
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