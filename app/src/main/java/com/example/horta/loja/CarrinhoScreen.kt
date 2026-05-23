package com.example.horta.loja

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CarrinhoScreen(onFinalizar: () -> Unit, onVoltar: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carrinho", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Produto 1 - R$ 10,00")
                Text("Produto 2 - R$ 20,00")
                Text("Total: R$ 30,00")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onFinalizar, modifier = Modifier.fillMaxWidth()) { Text("Finalizar Compra") }
        TextButton(onClick = onVoltar) { Text("Voltar") }
    }
}