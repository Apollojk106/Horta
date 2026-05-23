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
fun CadastroScreen(
    onCadastroSuccess: () -> Unit,
    onVoltarClick: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Criar Conta", fontSize = 32.sp, color = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = senha, onValueChange = { senha = it }, label = { Text("Senha") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = confirmarSenha, onValueChange = { confirmarSenha = it }, label = { Text("Confirmar senha") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    nome.isBlank() -> Toast.makeText(context, "Digite seu nome!", Toast.LENGTH_SHORT).show()
                    email.isBlank() -> Toast.makeText(context, "Digite seu e-mail!", Toast.LENGTH_SHORT).show()
                    senha.isBlank() -> Toast.makeText(context, "Digite uma senha!", Toast.LENGTH_SHORT).show()
                    senha != confirmarSenha -> Toast.makeText(context, "Senhas não coincidem!", Toast.LENGTH_SHORT).show()
                    else -> { Toast.makeText(context, "Cadastro realizado!", Toast.LENGTH_LONG).show(); onCadastroSuccess() }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) { Text("Cadastrar") }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onVoltarClick) { Text("Voltar para Login") }
    }
}