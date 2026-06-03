package com.example.horta.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.R
import com.example.horta.database.ClienteRepository
import com.example.horta.ui.components.HeaderLogin
import kotlinx.coroutines.*

@Composable
fun CadastroScreen(
    onCadastroSuccess: () -> Unit,
    onVoltarClick: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val repository = remember { ClienteRepository(context) }

    fun isSenhaForte(senha: String): Boolean {
        val temLetra = senha.any { it.isLetter() }
        val temNumero = senha.any { it.isDigit() }
        return senha.length >= 6 && temLetra && temNumero
    }

    fun fazerCadastro() {
        when {
            nome.isBlank() -> Toast.makeText(context, "Digite seu nome completo!", Toast.LENGTH_SHORT).show()
            email.isBlank() -> Toast.makeText(context, "Digite seu e-mail!", Toast.LENGTH_SHORT).show()
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                Toast.makeText(context, "Digite um e-mail válido!", Toast.LENGTH_SHORT).show()
            senha.isBlank() -> Toast.makeText(context, "Digite uma senha!", Toast.LENGTH_SHORT).show()
            senha.length < 6 -> Toast.makeText(context, "A senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show()
            !isSenhaForte(senha) -> Toast.makeText(context, "A senha deve conter letras E números!", Toast.LENGTH_SHORT).show()
            senha != confirmarSenha -> Toast.makeText(context, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
            else -> {
                isLoading = true
                GlobalScope.launch(Dispatchers.IO) {
                    val emailExistente = repository.emailExiste(email)

                    withContext(Dispatchers.Main) {
                        isLoading = false
                        if (emailExistente) {
                            Toast.makeText(context, "Este e-mail já está cadastrado!", Toast.LENGTH_SHORT).show()
                        } else {
                            val success = repository.cadastrar(nome, email, senha, telefone)
                            if (success) {
                                Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show()
                                onCadastroSuccess()
                            } else {
                                Toast.makeText(context, "Erro ao cadastrar. Tente novamente.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderLogin()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Criar sua conta",
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = DinamicTypography.displayMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Preencha seus dados para continuar",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = DinamicTypography.bodySmall,
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome completo") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    focusedLabelColor = Color(0xFF4CAF50)
                ),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))

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

            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                label = { Text("Telefone (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    focusedLabelColor = Color(0xFF4CAF50)
                ),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))

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
                ),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (senha.length >= 6) "✅" else "❌",
                            fontSize = 12.sp,
                            color = if (senha.length >= 6) Color(0xFF4CAF50) else Color.Gray
                        )
                        Text(" Mínimo de 6 caracteres", fontSize = 14.sp, color = Color.Gray)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (senha.any { it.isLetter() }) "✅" else "❌",
                            fontSize = 12.sp,
                            color = if (senha.any { it.isLetter() }) Color(0xFF4CAF50) else Color.Gray
                        )
                        Text(" Pelo menos uma letra", fontSize = 14.sp, color = Color.Gray)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (senha.any { it.isDigit() }) "✅" else "❌",
                            fontSize = 12.sp,
                            color = if (senha.any { it.isDigit() }) Color(0xFF4CAF50) else Color.Gray
                        )
                        Text(" Pelo menos um número", fontSize = 14.sp, color = Color.Gray)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (senha.isNotBlank() && senha == confirmarSenha) "✅" else "❌",
                            fontSize = 12.sp,
                            color = if (senha.isNotBlank() && senha == confirmarSenha) Color(0xFF4CAF50) else Color.Gray
                        )
                        Text(" Senhas coincidem", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { fazerCadastro() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading && isSenhaForte(senha) && senha == confirmarSenha
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.folhaicon),
                        contentDescription = "Folha",
                        modifier = Modifier.size(30.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cadastrar",
                        style = DinamicTypography.bodyLarge,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                    style = DinamicTypography.bodySmall
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onVoltarClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF4CAF50)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Voltar para Login",
                    fontSize = 16.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}