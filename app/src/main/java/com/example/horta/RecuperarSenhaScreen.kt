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
import com.example.horta.database.ClienteRepository
import com.example.horta.database.TokenRecuperacaoRepository
import kotlinx.coroutines.*

@Composable
fun RecuperarSenhaScreen(onVoltarClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var mostrarDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var tokenEnviado by remember { mutableStateOf(false) }
    var clienteId by remember { mutableStateOf(0L) }
    var tokenId by remember { mutableStateOf(0L) }

    val context = LocalContext.current
    val clienteRepo = remember { ClienteRepository(context) }
    val tokenRepo = remember { TokenRecuperacaoRepository(context) }

    // Função para verificar se a senha é forte (letras + números)
    fun isSenhaForte(senha: String): Boolean {
        val temLetra = senha.any { it.isLetter() }
        val temNumero = senha.any { it.isDigit() }
        return senha.length >= 6 && temLetra && temNumero
    }

    // Função para solicitar token
    fun solicitarToken() {
        if (email.isBlank()) {
            Toast.makeText(context, "Digite seu e-mail!", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true

        GlobalScope.launch(Dispatchers.IO) {
            val cliente = clienteRepo.getClienteByEmail(email)

            withContext(Dispatchers.Main) {
                isLoading = false

                if (cliente != null) {
                    val novoToken = tokenRepo.criarToken(cliente.id)
                    tokenEnviado = true
                    clienteId = cliente.id

                    Toast.makeText(
                        context,
                        "Token: $novoToken\n(Enviado por e-mail)",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(context, "E-mail não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Função para validar token
    fun validarToken() {
        if (token.isBlank()) {
            Toast.makeText(context, "Digite o token recebido!", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true

        GlobalScope.launch(Dispatchers.IO) {
            val cliente = clienteRepo.getClienteByEmail(email)

            withContext(Dispatchers.Main) {
                isLoading = false

                if (cliente == null) {
                    Toast.makeText(context, "E-mail não encontrado!", Toast.LENGTH_SHORT).show()
                    return@withContext
                }

                val tokenValido = tokenRepo.isTokenValido(token, cliente.id)
                val tokenObj = tokenRepo.getTokenRecuperacao(token, cliente.id)

                if (tokenValido && tokenObj != null) {
                    clienteId = cliente.id
                    tokenId = tokenObj.idToken
                    mostrarDialog = true
                } else {
                    Toast.makeText(context, "Token inválido ou expirado!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Função para redefinir senha
    fun redefinirSenha(novaSenha: String, confirmarSenha: String) {
        when {
            novaSenha.isBlank() -> Toast.makeText(context, "Digite uma nova senha!", Toast.LENGTH_SHORT).show()
            novaSenha.length < 6 -> Toast.makeText(context, "A senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show()
            !isSenhaForte(novaSenha) -> Toast.makeText(context, "A senha deve conter letras E números!", Toast.LENGTH_SHORT).show()
            novaSenha != confirmarSenha -> Toast.makeText(context, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
            else -> {
                isLoading = true

                GlobalScope.launch(Dispatchers.IO) {
                    val sucesso = clienteRepo.atualizarSenha(clienteId, novaSenha)

                    if (sucesso) {
                        tokenRepo.marcarTokenComoUsado(tokenId)
                        withContext(Dispatchers.Main) {
                            isLoading = false
                            Toast.makeText(context, "Senha alterada com sucesso!", Toast.LENGTH_LONG).show()
                            mostrarDialog = false
                            onVoltarClick()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            isLoading = false
                            Toast.makeText(context, "Erro ao alterar senha!", Toast.LENGTH_SHORT).show()
                        }
                    }
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
            text = "Recuperar Senha",
            fontSize = 32.sp,
            color = Color(0xFF4CAF50)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Digite seu e-mail para receber o token",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4CAF50),
                focusedLabelColor = Color(0xFF4CAF50)
            ),
            enabled = !isLoading && !tokenEnviado
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { solicitarToken() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && !tokenEnviado
        ) {
            if (isLoading && !tokenEnviado) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Solicitar Token", fontSize = 16.sp)
            }
        }

        if (tokenEnviado) {
            Spacer(modifier = Modifier.height(24.dp))

            Divider(color = Color.LightGray)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Digite o token recebido",
                fontSize = 14.sp,
                color = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = token,
                onValueChange = { token = it },
                label = { Text("Token") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    focusedLabelColor = Color(0xFF4CAF50)
                ),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { validarToken() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Validar Token", fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onVoltarClick,
            enabled = !isLoading
        ) {
            Text("Voltar para Login", fontSize = 14.sp, color = Color(0xFF4CAF50))
        }
    }

    // Dialog para redefinir senha
    if (mostrarDialog) {
        var novaSenha by remember { mutableStateOf("") }
        var confirmarSenha by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { mostrarDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Redefinir Senha",
                        fontSize = 24.sp,
                        color = Color(0xFF4CAF50)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Digite sua nova senha",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = novaSenha,
                        onValueChange = { novaSenha = it },
                        label = { Text("Nova senha") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            focusedLabelColor = Color(0xFF4CAF50)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = confirmarSenha,
                        onValueChange = { confirmarSenha = it },
                        label = { Text("Confirmar senha") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            focusedLabelColor = Color(0xFF4CAF50)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { mostrarDialog = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = { redefinirSenha(novaSenha, confirmarSenha) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            } else {
                                Text("Confirmar")
                            }
                        }
                    }
                }
            }
        }
    }
}