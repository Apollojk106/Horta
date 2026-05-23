package com.example.horta.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DoacaoScreen(onVoltar: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("🎁 Doação", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ajude a comunidade fazendo uma doação")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onVoltar, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
    }
}