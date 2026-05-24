package com.example.horta.ui.theme

import android.content.Context
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
import com.example.horta.database.ClienteRepository
import kotlinx.coroutines.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onCadastroClick: () -> Unit,
    onEsqueciSenhaClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val repository = remember { ClienteRepository(context) }

    fun fazerLogin() {
        if (email.isBlank()) {
            Toast.makeText(context, "Digite seu e-mail!", Toast.LENGTH_SHORT).show()
            return
        }
        if (senha.isBlank()) {
            Toast.makeText(context, "Digite sua senha!", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true

        GlobalScope.launch(Dispatchers.IO) {
            val (cliente, token) = repository.login(email, senha)

            withContext(Dispatchers.Main) {
                isLoading = false
                if (cliente != null && token != null) {
                    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("auth_token", token).apply()

                    Toast.makeText(context, "Bem-vindo, ${cliente.nome}!", Toast.LENGTH_LONG).show()
                    onLoginSuccess()
                } else {
                    Toast.makeText(context, "E-mail ou senha inválidos!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            fontSize = 32.sp,
            color = Color(0xFF4CAF50)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Faça login para continuar",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo E-mail
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4CAF50),
                focusedLabelColor = Color(0xFF4CAF50)
            ),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Senha
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4CAF50),
                focusedLabelColor = Color(0xFF4CAF50)
            ),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botão Esqueci a senha
        TextButton(
            onClick = onEsqueciSenhaClick,
            modifier = Modifier.align(Alignment.End),
            enabled = !isLoading
        ) {
            Text(
                text = "Esqueci minha senha?",
                fontSize = 12.sp,
                color = Color(0xFF4CAF50)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Entrar
        Button(
            onClick = { fazerLogin() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Entrar",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Linha divisória
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = Color.LightGray
            )
            Text(
                text = " ou ",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 12.sp,
                color = Color.Gray
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botão Criar nova conta
        Button(
            onClick = onCadastroClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFF4CAF50)
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(
                text = "Criar nova conta",
                fontSize = 16.sp,
                color = Color(0xFF4CAF50)
            )
        }
    }
}