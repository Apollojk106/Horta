package com.example.horta

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.components.BottomNavBar
import com.example.horta.database.ClienteRepository
import com.example.horta.database.SessaoRepository
import kotlinx.coroutines.*

@Composable
fun PerfilScreen(
    onNavigateTo: (String) -> Unit,
    onLogout: () -> Unit
) {
    val currentRoute = "perfil"
    val context = LocalContext.current

    var nome by remember { mutableStateOf("Carregando...") }
    var email by remember { mutableStateOf("Carregando...") }
    var dataCad by remember { mutableStateOf("Carregando...") }
    var isLoading by remember { mutableStateOf(true) }

    // Buscar dados do usuário logado
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val sessaoRepo = SessaoRepository(context)
                val clienteRepo = ClienteRepository(context)

                // Buscar sessão ativa
                val sessao = sessaoRepo.getUltimaSessaoAtiva()

                if (sessao != null) {
                    // Buscar dados do cliente
                    val cliente = clienteRepo.getClienteById(sessao.idCliente)

                    withContext(Dispatchers.Main) {
                        if (cliente != null) {
                            nome = cliente.nome
                            email = cliente.email
                            dataCad = cliente.dataCad ?: "2024"
                        } else {
                            nome = "Usuário não encontrado"
                            email = "---"
                            dataCad = "---"
                        }
                        isLoading = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        nome = "Nenhum usuário logado"
                        email = "---"
                        dataCad = "---"
                        isLoading = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    nome = "Erro ao carregar"
                    email = e.message ?: "Erro"
                    dataCad = "---"
                    isLoading = false
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    when (route) {
                        "inicio" -> onNavigateTo("inicio")
                        "horta" -> onNavigateTo("horta")
                        "loja" -> onNavigateTo("loja")
                        "doacao" -> onNavigateTo("doacao")
                        "pedidos" -> onNavigateTo("pedidos")
                        "perfil" -> { /* Já está no perfil */ }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🔍 TEXTO PARA IDENTIFICAR A TELA (remova depois)
            Text(
                text = "📍 TELA: PERFIL",
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text("👤", fontSize = 80.sp)
            Text("Perfil", fontSize = 32.sp, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(32.dp))

            // Card com informações do usuário
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (isLoading) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Carregando dados...")
                        }
                    } else {
                        Text(
                            text = "👤 Nome: $nome",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = "📧 Email: $email",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Botão Sair
            Button(
                onClick = {
                    try {
                        val sessaoRepo = SessaoRepository(context)
                        val sessao = sessaoRepo.getUltimaSessaoAtiva()

                        if (sessao != null) {
                            val encerrou = sessaoRepo.encerrarSessaoPorId(sessao.idSessao)
                            if (encerrou) {
                                Toast.makeText(context, "Sessão encerrada com sucesso!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Erro ao encerrar sessão", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Nenhuma sessão ativa encontrada", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                    // Chamar logout do navigation
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sair", fontSize = 18.sp)
            }
        }
    }
}