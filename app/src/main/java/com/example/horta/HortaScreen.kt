package com.example.horta.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HortaScreen(onVoltar: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("🌱 Horta", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Aqui você acompanha sua horta")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onVoltar, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
    }
}