package com.example.horta

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.horta.components.BottomNavBar
import com.example.horta.database.ClienteRepository
import com.example.horta.database.EnderecoRepository
import com.example.horta.database.PedidoRepository
import com.example.horta.database.SessaoRepository
import kotlinx.coroutines.*

@Composable
fun PerfilScreen(
    onNavigateTo: (String) -> Unit,
    onLogout: () -> Unit
) {
    val currentRoute = "perfil"
    val context = LocalContext.current

    var mostrarDialogDados by remember { mutableStateOf(false) }
    var mostrarDialogSeguranca by remember { mutableStateOf(false) }

    var nomeUsuario by remember { mutableStateOf("Carregando...") }
    var emailUsuario by remember { mutableStateOf("Carregando...") }
    var clienteId by remember { mutableStateOf(0L) }
    var isLoading by remember { mutableStateOf(true) }

    var enderecoRua by remember { mutableStateOf("") }
    var enderecoNumero by remember { mutableStateOf("") }
    var enderecoBairro by remember { mutableStateOf("") }
    var enderecoCidade by remember { mutableStateOf("") }
    var enderecoEstado by remember { mutableStateOf("") }
    var enderecoCep by remember { mutableStateOf("") }
    var enderecoComplemento by remember { mutableStateOf("") }

    var totalPedidos by remember { mutableStateOf(0) }
    var pedidosEmAndamento by remember { mutableStateOf(0) }
    var pedidosEntregues by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val sessaoRepo = SessaoRepository(context)
                val clienteRepo = ClienteRepository(context)
                val enderecoRepo = EnderecoRepository(context)
                val pedidoRepo = PedidoRepository(context)

                val sessao = sessaoRepo.getUltimaSessaoAtiva()

                if (sessao != null) {
                    val cliente = clienteRepo.getClienteById(sessao.idCliente)

                    withContext(Dispatchers.Main) {
                        if (cliente != null) {
                            clienteId = cliente.id
                            nomeUsuario = cliente.nome
                            emailUsuario = cliente.email
                        } else {
                            nomeUsuario = "Usuário"
                            emailUsuario = "email@exemplo.com"
                        }
                        isLoading = false
                    }

                    val endereco = enderecoRepo.getEnderecoByClienteId(sessao.idCliente)
                    withContext(Dispatchers.Main) {
                        if (endereco != null) {
                            enderecoRua = endereco.rua
                            enderecoNumero = endereco.numero
                            enderecoBairro = endereco.bairro
                            enderecoCidade = endereco.cidade
                            enderecoEstado = endereco.estado
                            enderecoCep = endereco.cep
                            enderecoComplemento = endereco.complemento
                        }
                    }

                    val pedidos = pedidoRepo.getPedidosByCliente(sessao.idCliente)
                    withContext(Dispatchers.Main) {
                        totalPedidos = pedidos.size
                        pedidosEmAndamento = pedidos.count { it.status == "Em andamento" }
                        pedidosEntregues = pedidos.count { it.status == "Entregue" }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        nomeUsuario = "Não logado"
                        emailUsuario = "faça login novamente"
                        isLoading = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    nomeUsuario = "Erro ao carregar"
                    emailUsuario = e.message ?: "Erro"
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
                        "home" -> onNavigateTo("home")
                        "horta" -> onNavigateTo("horta")
                        "loja" -> onNavigateTo("loja")
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fundos),
                    contentDescription = "Fundo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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

                    Column {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = nomeUsuario,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = emailUsuario,
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = totalPedidos.toString(),
                        label = "Pedidos"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = pedidosEmAndamento.toString(),
                        label = "Em andamento"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = pedidosEntregues.toString(),
                        label = "Entregues"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        MenuOption(
                            iconRes = R.drawable.mapsicon,
                            title = "Dados pessoais",
                            subtitle = "Atualize suas informações",
                            onClick = { mostrarDialogDados = true },
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        MenuOption(
                            iconRes = R.drawable.lockicon,
                            title = "Segurança",
                            subtitle = "Alterar sua senha e configuração",
                            onClick = { mostrarDialogSeguranca = true }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, Color(0xFFFF0000))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logouticon),
                            contentDescription = "Sair",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = "Sair",
                            fontSize = 20.sp,
                            color = Color(0xFFFF0000)
                        )
                    }
                }
            }
        }
    }

    if (mostrarDialogDados) {
        var localNome by remember { mutableStateOf(nomeUsuario) }
        var localRua by remember { mutableStateOf(enderecoRua) }
        var localNumero by remember { mutableStateOf(enderecoNumero) }
        var localBairro by remember { mutableStateOf(enderecoBairro) }
        var localCidade by remember { mutableStateOf(enderecoCidade) }
        var localEstado by remember { mutableStateOf(enderecoEstado) }
        var localCep by remember { mutableStateOf(enderecoCep) }
        var localComplemento by remember { mutableStateOf(enderecoComplemento) }
        var isSaving by remember { mutableStateOf(false) }

        Dialog(onDismissRequest = { mostrarDialogDados = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .heightIn(max = 500.dp)
                    .verticalScroll(rememberScrollState()),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Dados pessoais",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = localNome,
                        onValueChange = { localNome = it },
                        label = { Text("Nome completo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Endereço",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = localRua,
                        onValueChange = { localRua = it },
                        label = { Text("Rua") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = localNumero,
                            onValueChange = { localNumero = it },
                            label = { Text("Número") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = localComplemento,
                            onValueChange = { localComplemento = it },
                            label = { Text("Complemento") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = localBairro,
                        onValueChange = { localBairro = it },
                        label = { Text("Bairro") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = localCidade,
                            onValueChange = { localCidade = it },
                            label = { Text("Cidade") },
                            modifier = Modifier.weight(2f)
                        )
                        OutlinedTextField(
                            value = localEstado,
                            onValueChange = { localEstado = it },
                            label = { Text("UF") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = localCep,
                        onValueChange = { localCep = it },
                        label = { Text("CEP") },
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
                            onClick = {
                                isSaving = true
                                CoroutineScope(Dispatchers.IO).launch {
                                    val clienteRepo = ClienteRepository(context)
                                    val enderecoRepo = EnderecoRepository(context)

                                    if (localNome != nomeUsuario) {
                                        clienteRepo.atualizarNome(clienteId, localNome)
                                    }

                                    enderecoRepo.salvarEndereco(
                                        clienteId,
                                        localRua,
                                        localNumero,
                                        localBairro,
                                        localCidade,
                                        localEstado,
                                        localCep,
                                        localComplemento
                                    )

                                    withContext(Dispatchers.Main) {
                                        nomeUsuario = localNome
                                        enderecoRua = localRua
                                        enderecoNumero = localNumero
                                        enderecoBairro = localBairro
                                        enderecoCidade = localCidade
                                        enderecoEstado = localEstado
                                        enderecoCep = localCep
                                        enderecoComplemento = localComplemento
                                        isSaving = false
                                        mostrarDialogDados = false
                                        Toast.makeText(context, "Dados salvos!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            enabled = !isSaving
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            } else {
                                Text("Salvar")
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogSeguranca) {
        var senhaAtual by remember { mutableStateOf("") }
        var novaSenha by remember { mutableStateOf("") }
        var confirmarSenha by remember { mutableStateOf("") }
        var isSaving by remember { mutableStateOf(false) }

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
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = senhaAtual,
                        onValueChange = { senhaAtual = it },
                        label = { Text("Senha atual") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = novaSenha,
                        onValueChange = { novaSenha = it },
                        label = { Text("Nova senha") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmarSenha,
                        onValueChange = { confirmarSenha = it },
                        label = { Text("Confirmar nova senha") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
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
                            onClick = {
                                when {
                                    novaSenha.isBlank() || confirmarSenha.isBlank() -> {
                                        Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                                    }
                                    novaSenha != confirmarSenha -> {
                                        Toast.makeText(context, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
                                    }
                                    novaSenha.length < 6 -> {
                                        Toast.makeText(context, "A senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        isSaving = true
                                        CoroutineScope(Dispatchers.IO).launch {
                                            val clienteRepo = ClienteRepository(context)
                                            val success = clienteRepo.atualizarSenha(clienteId, novaSenha)
                                            withContext(Dispatchers.Main) {
                                                isSaving = false
                                                if (success) {
                                                    Toast.makeText(context, "Senha alterada com sucesso!", Toast.LENGTH_LONG).show()
                                                    mostrarDialogSeguranca = false
                                                } else {
                                                    Toast.makeText(context, "Erro ao alterar senha!", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            enabled = !isSaving
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            } else {
                                Text("Alterar")
                            }
                        }
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
fun MenuOption(
    iconRes: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(22.dp),
                colorFilter = ColorFilter.tint(Color(0xFF4CAF50))
            )
        }

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