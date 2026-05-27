package com.example.horta.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.components.BottomNavBar
import com.example.horta.ui.components.BaseScreen

@Composable
fun HortaScreen(
    currentRoute: String = "horta",
    onNavigateTo: (String) -> Unit,
    onLogout: () -> Unit
) {

    Scaffold(

        bottomBar = {

            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = onNavigateTo
            )
        }

    ) { paddingValues ->

        BaseScreen(
            titulo = "HORTA",
            subtitulo = "Acompanhe nossas contribuições"
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // STATUS DA HORTA
                Card(
                    modifier = Modifier.fillMaxWidth(),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "🌱 Situação Atual",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "• Temperatura ideal",
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "• Solo úmido",
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "• Irrigação concluída",
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "• 85% de crescimento",
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // RESULTADOS
                Text(
                    text = "Resultados da Horta",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF1F8E9)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "🥬 Alface",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Produção estimada: 12 unidades"
                        )

                        Text(
                            text = "Colheita prevista: 5 dias"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF1F8E9)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "🍅 Tomate",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Produção estimada: 8kg"
                        )

                        Text(
                            text = "Colheita prevista: 10 dias"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // BOTÕES
                Button(
                    onClick = { onNavigateTo("loja") },

                    modifier = Modifier.fillMaxWidth(),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {

                    Text("Ir para Loja")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onLogout,

                    modifier = Modifier.fillMaxWidth(),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336)
                    )
                ) {

                    Text("Sair")
                }
            }
        }
    }
}