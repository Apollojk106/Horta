package com.example.horta.loja

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
import com.example.horta.database.EnderecoRepository
import com.example.horta.database.SessaoRepository
import com.example.horta.ui.components.LojaBaseScreen
import com.example.horta.ui.components.TopBarCompleta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EntregaScreen(
    onNavigateTo: (String) -> Unit,
    onProximo: () -> Unit,
    onVoltar: () -> Unit,
    onVerCarrinho: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sessaoRepo = SessaoRepository(context)
    val enderecoRepo = EnderecoRepository(context)

    var endereco by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var complemento by remember { mutableStateOf("") }

    var clienteId by remember { mutableStateOf(0L) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var mostrarDialogLimpar by remember { mutableStateOf(false) }

    val currentRoute = "loja"

    LaunchedEffect(Unit) {
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val sessao = sessaoRepo.getUltimaSessaoAtiva()
                    if (sessao != null) {
                        clienteId = sessao.idCliente
                        val enderecoSalvo = enderecoRepo.getEnderecoByClienteId(sessao.idCliente)
                        withContext(Dispatchers.Main) {
                            if (enderecoSalvo != null) {
                                endereco = enderecoSalvo.rua
                                numero = enderecoSalvo.numero
                                bairro = enderecoSalvo.bairro
                                cidade = enderecoSalvo.cidade
                                estado = enderecoSalvo.estado
                                cep = enderecoSalvo.cep
                                complemento = enderecoSalvo.complemento
                            }
                            isLoading = false
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            isLoading = false
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        isLoading = false
                        Toast.makeText(context, "Erro ao carregar endereço", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun salvarEndereco() {
        if (endereco.isBlank() || numero.isBlank() || bairro.isBlank() || cidade.isBlank() || cep.isBlank()) {
            Toast.makeText(context, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
            return
        }

        isSaving = true
        scope.launch {
            withContext(Dispatchers.IO) {
                val success = enderecoRepo.salvarEndereco(
                    clienteId,
                    endereco,
                    numero,
                    bairro,
                    cidade,
                    estado,
                    cep,
                    complemento
                )
                withContext(Dispatchers.Main) {
                    isSaving = false
                    if (success) {
                        Toast.makeText(context, "Endereço salvo com sucesso!", Toast.LENGTH_SHORT).show()
                        onProximo()
                    } else {
                        Toast.makeText(context, "Erro ao salvar endereço", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun limparCampos() {
        endereco = ""
        numero = ""
        bairro = ""
        cidade = ""
        estado = ""
        cep = ""
        complemento = ""
        mostrarDialogLimpar = false
        Toast.makeText(context, "Campos limpos!", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = onNavigateTo
            )
        }
    ) { paddingValues ->
        LojaBaseScreen(
            onCarrinhoClick = onVerCarrinho
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                TopBarCompleta(
                    titulo = "Endereço de Entrega",
                    onVoltarClick = onVoltar,
                    onLimparClick = { mostrarDialogLimpar = true }
                )

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = endereco,
                            onValueChange = { endereco = it },
                            label = { Text("Rua *") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4CAF50),
                                focusedLabelColor = Color(0xFF4CAF50)
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = numero,
                                onValueChange = { numero = it },
                                label = { Text("Número *") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4CAF50),
                                    focusedLabelColor = Color(0xFF4CAF50)
                                )
                            )
                            OutlinedTextField(
                                value = complemento,
                                onValueChange = { complemento = it },
                                label = { Text("Complemento") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4CAF50),
                                    focusedLabelColor = Color(0xFF4CAF50)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = bairro,
                            onValueChange = { bairro = it },
                            label = { Text("Bairro *") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4CAF50),
                                focusedLabelColor = Color(0xFF4CAF50)
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = cidade,
                                onValueChange = { cidade = it },
                                label = { Text("Cidade *") },
                                modifier = Modifier.weight(2f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4CAF50),
                                    focusedLabelColor = Color(0xFF4CAF50)
                                )
                            )
                            OutlinedTextField(
                                value = estado,
                                onValueChange = { estado = it },
                                label = { Text("UF") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4CAF50),
                                    focusedLabelColor = Color(0xFF4CAF50)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = cep,
                            onValueChange = { cep = it },
                            label = { Text("CEP *") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4CAF50),
                                focusedLabelColor = Color(0xFF4CAF50)
                            )
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { salvarEndereco() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            enabled = !isSaving
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            } else {
                                Text("Salvar e Continuar", fontSize = 18.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = onVoltar,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Voltar", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogLimpar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogLimpar = false },
            title = { Text("Limpar campos", color = Color(0xFFF44336)) },
            text = { Text("Tem certeza que deseja limpar todos os campos?") },
            confirmButton = {
                Button(
                    onClick = { limparCampos() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text("Sim, limpar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogLimpar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}