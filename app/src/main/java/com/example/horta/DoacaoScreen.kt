package com.example.horta.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.components.BottomNavBar
import com.example.horta.ui.components.BaseScreen

@Composable
fun DoacaoScreen(
    currentRoute: String = "doacao",
    onNavigateTo: (String) -> Unit
) {

    var valor by remember { mutableStateOf("") }

    Scaffold(

        bottomBar = {

            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = onNavigateTo
            )
        }

    ) { paddingValues ->

        BaseScreen(
            titulo = "DOAÇÃO",
            subtitulo = "Ajude a comunidade"
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // CARD PRINCIPAL
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
                            text = "🌱 Faça uma doação",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Sua contribuição ajuda no cultivo, manutenção e distribuição de alimentos.",
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = valor,
                            onValueChange = { valor = it },

                            label = {
                                Text("Valor da doação")
                            },

                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {

                            },

                            modifier = Modifier.fillMaxWidth(),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            )
                        ) {

                            Text(
                                text = "Doar Agora",
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // METAS
                Text(
                    text = "Metas da Comunidade",
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
                            text = "💧 Sistema de irrigação",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("70% arrecadado")
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
                            text = "🌿 Expansão da horta",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )


                        Text("40% arrecadado")
                    }
                }
            }
        }
    }
}