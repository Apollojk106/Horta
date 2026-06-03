package com.example.horta.ui.theme

import android.content.Context
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
import com.example.horta.R
import com.example.horta.database.ClienteRepository
import kotlinx.coroutines.*
import com.example.horta.ui.components.HeaderLogin

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
                text = "Bem-vindo(a)!",
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = DinamicTypography.displayMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Faça login para continuar",
                style = DinamicTypography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

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

            Spacer(modifier = Modifier.height(4.dp))

            TextButton(
                onClick = onEsqueciSenhaClick,
                modifier = Modifier.align(Alignment.End),
                enabled = !isLoading
            ) {
                Text(
                    text = "Esqueci minha senha?",
                    style = DinamicTypography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { fazerLogin() },
                colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.folhaicon),
                            contentDescription = "Folha",
                            modifier = Modifier.size(30.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Entrar",
                            style = DinamicTypography.bodyLarge,
                            color = Color.White
                        )
                    }
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
                onClick = onCadastroClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF4CAF50)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Criar nova conta",
                    style = DinamicTypography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}