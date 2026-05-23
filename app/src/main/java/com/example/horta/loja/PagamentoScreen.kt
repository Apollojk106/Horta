package com.example.horta.loja

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PagamentoScreen(onConfirmar: () -> Unit, onVoltar: () -> Unit) {
    var selectedOption by remember { mutableStateOf("credito") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Pagamento", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            RadioButton(selected = selectedOption == "credito", onClick = { selectedOption = "credito" })
            Text("Cartão de Crédito", modifier = Modifier.padding(start = 8.dp))
        }
        Row {
            RadioButton(selected = selectedOption == "pix", onClick = { selectedOption = "pix" })
            Text("PIX", modifier = Modifier.padding(start = 8.dp))
        }
        Row {
            RadioButton(selected = selectedOption == "boleto", onClick = { selectedOption = "boleto" })
            Text("Boleto", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onConfirmar, modifier = Modifier.fillMaxWidth()) { Text("Confirmar Pagamento") }
        TextButton(onClick = onVoltar) { Text("Voltar") }
    }
}