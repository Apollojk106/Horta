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

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onCadastroClick: () -> Unit,
    onEsqueciSenhaClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", fontSize = 32.sp, color = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha, onValueChange = { senha = it },
            label = { Text("Senha") }, visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onEsqueciSenhaClick, modifier = Modifier.align(Alignment.End)) {
            Text("Esqueci minha senha?", fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && senha.isNotBlank()) {
                    Toast.makeText(context, "Login realizado!", Toast.LENGTH_LONG).show()
                    onLoginSuccess()
                } else {
                    Toast.makeText(context, "Preencha e-mail e senha!", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) { Text("Entrar", fontSize = 18.sp) }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCadastroClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Criar nova conta", color = Color(0xFF4CAF50)) }
    }
}