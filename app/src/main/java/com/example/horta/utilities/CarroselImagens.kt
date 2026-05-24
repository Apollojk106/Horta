package com.example.horta.utilities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CarrosselImagens(
    modifier: Modifier = Modifier,
    imagens: List<Int>,
    intervalo: Long = 3000L
) {
    var indiceAtual by remember { mutableStateOf(0) }

    // Animação automática
    LaunchedEffect(Unit) {
        while (true) {
            delay(intervalo)
            indiceAtual = (indiceAtual + 1) % imagens.size
        }
    }

    Card(
        modifier = modifier
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8F5E9))
        ) {
            // Imagem atual
            Image(
                painter = painterResource(id = imagens[indiceAtual]),
                contentDescription = "Imagem do carrossel",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Indicadores de página (pontinhos)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                imagens.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (indiceAtual == index) Color(0xFF4CAF50)
                                else Color.White.copy(alpha = 0.5f)
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}