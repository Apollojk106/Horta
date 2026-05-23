package com.example.horta

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InicioScreen(onComecarClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🌱", fontSize = 80.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("HORTA", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.height(8.dp))
        Text("Cultivando um futuro melhor", fontSize = 18.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onComecarClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Começar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}