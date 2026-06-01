package com.example.horta.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.R
import com.example.horta.database.CarrinhoRepository
import com.example.horta.database.SessaoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LojaBaseScreen(
    onCarrinhoClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var quantidadeProdutos by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val sessaoRepo = SessaoRepository(context)
                    val carrinhoRepo = CarrinhoRepository(context)

                    val sessao = sessaoRepo.getUltimaSessaoAtiva()
                    if (sessao != null) {
                        val itens = carrinhoRepo.buscarTodosItens()
                        quantidadeProdutos = itens.sumOf { it.quantidade }
                    }
                } catch (e: Exception) {
                    quantidadeProdutos = 0
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Header com imagem de fundo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.fundos),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Loja da horta",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = onCarrinhoClick,
                            modifier = Modifier.size(60.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.lojaicon),
                                contentDescription = "Carrinho",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$quantidadeProdutos",
                                color = Color(0xFF4CAF50),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Conteúdo principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                content()
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}