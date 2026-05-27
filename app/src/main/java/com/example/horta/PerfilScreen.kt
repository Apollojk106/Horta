package com.example.horta

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.horta.components.BottomNavBar

@Composable
fun PerfilScreen(
    onNavigateTo: (String) -> Unit,
    onLogout: () -> Unit
) {
    val currentRoute = "perfil"
    var mostrarDialogDados by remember { mutableStateOf(false) }
    var mostrarDialogPedidos by remember { mutableStateOf(false) }
    var mostrarDialogSeguranca by remember { mutableStateOf(false) }
    var endereco by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    when (route) {
                        "home" -> onNavigateTo("home")
                        "horta" -> onNavigateTo("horta")
                        "loja" -> onNavigateTo("loja")
                        "doacao" -> onNavigateTo("doacao")
                        "pedidos" -> onNavigateTo("pedidos")
                        "perfil" -> { /* Já está */ }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF5F5F5))
        ) {
            // Header com imagem de fundo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                // Imagem de fundo
                Image(
                    painter = painterResource(id = R.drawable.fundos),
                    contentDescription = "Fundo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Conteúdo sobre a imagem (ícone à esquerda + nome/email)
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ícone de perfil circular com fundo branco
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.perfilicon),
                            contentDescription = "Perfil",
                            modifier = Modifier.size(45.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Nome e email (à direita do ícone)
                    Column {
                        Text(
                            text = "Apollo Henrique",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "apollohenrique@gmail.com",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Conteúdo (com fundo branco)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                // Cards de estatísticas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = "12",
                        label = "Pedidos"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = "3",
                        label = "Em andamento"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = "9",
                        label = "Entregues"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Menu de opções
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        // Opção 1: Dados pessoais
                        MenuOption(
                            icon = "👤",
                            title = "Dados pessoais",
                            subtitle = "Atualize suas informações",
                            onClick = { mostrarDialogDados = true }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        // Opção 2: Meus pedidos
                        MenuOption(
                            icon = "📦",
                            title = "Meus pedidos",
                            subtitle = "Acompanhe seus pedidos",
                            onClick = { mostrarDialogPedidos = true }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        // Opção 3: Segurança
                        MenuOption(
                            icon = "🔒",
                            title = "Segurança",
                            subtitle = "Alterar sua senha e configuração",
                            onClick = { mostrarDialogSeguranca = true }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botão Sair
                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Sair", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }

    // Dialogs (mantidos iguais)
    if (mostrarDialogDados) {
        Dialog(onDismissRequest = { mostrarDialogDados = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Dados pessoais",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = endereco,
                        onValueChange = { endereco = it },
                        label = { Text("Endereço") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { mostrarDialogDados = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) { Text("Cancelar") }
                        Button(
                            onClick = { mostrarDialogDados = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) { Text("Salvar") }
                    }
                }
            }
        }
    }

    if (mostrarDialogPedidos) {
        Dialog(onDismissRequest = { mostrarDialogPedidos = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Meus pedidos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("• Pedido #001 - R$ 45,00 - Entregue")
                    Text("• Pedido #002 - R$ 32,50 - Entregue")
                    Text("• Pedido #003 - R$ 78,90 - Em andamento")
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { mostrarDialogPedidos = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) { Text("Fechar") }
                }
            }
        }
    }

    if (mostrarDialogSeguranca) {
        Dialog(onDismissRequest = { mostrarDialogSeguranca = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Segurança",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Alterar senha")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Senha atual") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Nova senha") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Confirmar nova senha") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { mostrarDialogSeguranca = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) { Text("Cancelar") }
                        Button(
                            onClick = { mostrarDialogSeguranca = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) { Text("Alterar") }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, number: String, label: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun MenuOption(icon: String, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2E7D32)
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
        Text(
            text = "›",
            fontSize = 20.sp,
            color = Color.Gray
        )
    }
}