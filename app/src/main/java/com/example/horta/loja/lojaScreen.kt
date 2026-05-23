package com.example.horta.loja

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LojaScreen(
    onVerCarrinho: () -> Unit,
    onVerHorta: () -> Unit,
    onVerDoacao: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🛒", fontSize = 64.sp)
        Text("Loja", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onVerCarrinho, modifier = Modifier.fillMaxWidth()) { Text("Ver Carrinho") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onVerHorta, modifier = Modifier.fillMaxWidth()) { Text("Horta") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onVerDoacao, modifier = Modifier.fillMaxWidth()) { Text("Doação") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text("Sair") }
    }
}