package com.example.horta

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilScreen(onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("👤", fontSize = 80.sp)
        Text("Perfil", fontSize = 32.sp, color = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.height(32.dp))
        Text("Email: usuario@email.com")
        Text("Nome: Usuário")
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onLogout) { Text("Sair") }
    }
}