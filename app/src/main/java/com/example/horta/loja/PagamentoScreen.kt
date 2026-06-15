package com.example.horta.loja

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.R
import com.example.horta.components.BottomNavBar
import com.example.horta.database.CarrinhoRepository
import com.example.horta.database.PedidoRepository
import com.example.horta.database.SessaoRepository
import com.example.horta.ui.components.LojaBaseScreen
import com.example.horta.ui.components.TopBarCompleta
import com.example.horta.ui.theme.DinamicTypography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun PagamentoScreen(
    onNavigateTo: (String) -> Unit,
    onConfirmar: () -> Unit,
    onVoltar: () -> Unit,
    onVerCarrinho: () -> Unit,
    onNavigateToQRCode: () -> Unit,
    totalCompra: Double = 45.00
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedPayment by remember { mutableStateOf("") }
    var showCartaoOptions by remember { mutableStateOf(false) }
    var selectedCartaoTipo by remember { mutableStateOf("") }
    var trocoValue by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

    val currentRoute = "loja"

    val sessaoRepo = SessaoRepository(context)
    val carrinhoRepo = CarrinhoRepository(context)
    val pedidoRepo = PedidoRepository(context)

    fun criarPedido() {
        val metodoPagamento = when {
            selectedPayment == "pix" -> "PIX"
            selectedPayment == "cartao" && selectedCartaoTipo.isNotEmpty() -> {
                if (selectedCartaoTipo == "credito") "Cartão de Crédito"
                else "Cartão de Débito"
            }
            selectedPayment == "dinheiro" -> "Dinheiro"
            else -> ""
        }

        if (metodoPagamento.isEmpty()) {
            Toast.makeText(context, "Selecione uma forma de pagamento!", Toast.LENGTH_SHORT).show()
            return
        }

        isProcessing = true

        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val sessao = sessaoRepo.getUltimaSessaoAtiva()
                    if (sessao == null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Usuário não logado!", Toast.LENGTH_SHORT).show()
                            isProcessing = false
                        }
                        return@withContext
                    }

                    val itensCarrinho = carrinhoRepo.buscarTodosItens()
                    if (itensCarrinho.isEmpty()) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Carrinho vazio!", Toast.LENGTH_SHORT).show()
                            isProcessing = false
                        }
                        return@withContext
                    }

                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_MONTH, 5)

                    val idPedido = pedidoRepo.criarPedido(
                        idCliente = sessao.idCliente,
                        itens = itensCarrinho.map { Pair(it.idProduto, it.quantidade) },
                        total = totalCompra,
                        metodoPagamento = metodoPagamento,
                        troco = if (selectedPayment == "dinheiro") trocoValue.toDoubleOrNull() ?: 0.0 else 0.0
                    )

                    if (idPedido > 0) {
                        carrinhoRepo.limparCarrinho()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Pedido #$idPedido criado com sucesso!",
                                Toast.LENGTH_LONG
                            ).show()
                            isProcessing = false

                            if (selectedPayment == "pix") {
                                onNavigateToQRCode()
                            } else {
                                onConfirmar()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Erro ao criar pedido!", Toast.LENGTH_SHORT).show()
                            isProcessing = false
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                        isProcessing = false
                    }
                }
            }
        }
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
                    titulo = "Opção de Pagamento",
                    onVoltarClick = onVoltar,
                    onLimparClick = { }
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total a pagar:",
                            style = DinamicTypography.bodySmall,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = "R$ ${String.format("%.2f", totalCompra)}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Selecione a forma de pagamento:",
                    style = DinamicTypography.bodySmall,
                    color = Color(0xFF2E7D32)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Opção PIX
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedPayment == "pix") Color(0xFF4CAF50).copy(alpha = 0.1f) else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedPayment = "pix"
                                showCartaoOptions = false
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPayment == "pix",
                            onClick = {
                                selectedPayment = "pix"
                                showCartaoOptions = false
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4CAF50))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PIX",
                            style = DinamicTypography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (showCartaoOptions) Color(0xFF4CAF50).copy(alpha = 0.1f) else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPayment = "cartao"
                                    showCartaoOptions = !showCartaoOptions
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = showCartaoOptions,
                                onClick = {
                                    selectedPayment = "cartao"
                                    showCartaoOptions = !showCartaoOptions
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4CAF50))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cartão",
                                style = DinamicTypography.bodySmall
                            )
                        }

                        if (showCartaoOptions) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 56.dp, end = 16.dp, bottom = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedCartaoTipo == "credito",
                                        onClick = { selectedCartaoTipo = "credito" },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4CAF50))
                                    )
                                    Text("Crédito", style = DinamicTypography.bodySmall)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedCartaoTipo == "debito",
                                        onClick = { selectedCartaoTipo = "debito" },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4CAF50))
                                    )
                                    Text("Débito", style = DinamicTypography.bodySmall)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedPayment == "dinheiro") Color(0xFF4CAF50).copy(alpha = 0.1f) else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPayment = "dinheiro"
                                    showCartaoOptions = false
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPayment == "dinheiro",
                                onClick = {
                                    selectedPayment = "dinheiro"
                                    showCartaoOptions = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4CAF50))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Dinheiro",
                                style = DinamicTypography.bodySmall
                            )
                        }

                        if (selectedPayment == "dinheiro") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 56.dp, end = 16.dp, bottom = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Troco para:",
                                    style = DinamicTypography.bodySmall,
                                    modifier = Modifier.width(80.dp)
                                )
                                OutlinedTextField(
                                    value = trocoValue,
                                    onValueChange = { trocoValue = it },
                                    label = { Text("EX: 50") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF4CAF50),
                                        focusedLabelColor = Color(0xFF4CAF50)
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { criarPedido() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    enabled = !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
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
                                text = "Confirmar Pedido",
                                style = DinamicTypography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}