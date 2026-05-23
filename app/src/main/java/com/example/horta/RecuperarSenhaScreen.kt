package com.example.horta

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun RecuperarSenhaScreen(onVoltarClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var mostrarDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Recuperar Senha", fontSize = 32.sp, color = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = token, onValueChange = { token = it }, label = { Text("Token") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && token.isNotBlank()) {
                    mostrarDialog = true
                } else {
                    Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Validar Token") }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onVoltarClick) { Text("Voltar para Login") }
    }

    if (mostrarDialog) {
        Dialog(onDismissRequest = { mostrarDialog = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Redefinir Senha", fontSize = 24.sp, color = Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.height(16.dp))
                    var novaSenha by remember { mutableStateOf("") }
                    var confirmar by remember { mutableStateOf("") }

                    OutlinedTextField(value = novaSenha, onValueChange = { novaSenha = it }, label = { Text("Nova senha") }, visualTransformation = PasswordVisualTransformation())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = confirmar, onValueChange = { confirmar = it }, label = { Text("Confirmar senha") }, visualTransformation = PasswordVisualTransformation())
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { mostrarDialog = false }, modifier = Modifier.weight(1f)) { Text("Cancelar") }
                        Button(onClick = {
                            if (novaSenha.isNotBlank() && novaSenha == confirmar) {
                                Toast.makeText(context, "Senha alterada!", Toast.LENGTH_LONG).show()
                                mostrarDialog = false
                                onVoltarClick()
                            } else {
                                Toast.makeText(context, "Senhas não coincidem!", Toast.LENGTH_SHORT).show()
                            }
                        }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) { Text("Confirmar") }
                    }
                }
            }
        }
    }
}