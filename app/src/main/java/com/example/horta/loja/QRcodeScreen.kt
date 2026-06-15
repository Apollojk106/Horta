package com.example.horta.loja

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.components.BottomNavBar
import com.example.horta.R
import com.example.horta.ui.components.LojaBaseScreen
import com.example.horta.ui.components.TopBarCompleta
import com.example.horta.ui.theme.DinamicTypography

@Composable
fun QRcodeScreen(
    onNavigateTo: (String) -> Unit,
    onConcluir: () -> Unit,
    onVerCarrinho: () -> Unit
) {
    val currentRoute = "loja"

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
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TopBarCompleta(
                    titulo = "QR Code",
                    onVoltarClick = {},
                    onLimparClick = { }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.size(200.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.qrcode),
                            contentDescription = "QR Code",
                            modifier = Modifier.size(150.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Escaneie o QR Code com seu banco",
                    style = DinamicTypography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onConcluir,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
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
                            text = "Continuar",
                            style = DinamicTypography.bodyLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}