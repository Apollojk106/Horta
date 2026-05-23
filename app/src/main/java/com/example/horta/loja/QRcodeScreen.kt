package com.example.horta.loja

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QRcodeScreen(onConcluir: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📱", fontSize = 128.sp)
        Text("QR Code", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Escaneie o código para pagamento", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onConcluir, modifier = Modifier.fillMaxWidth()) { Text("Concluir Compra") }
    }
}