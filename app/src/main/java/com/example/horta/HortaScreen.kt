package com.example.horta.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Impacto da comunidade",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Veja como sua ajuda transforma vidas e a nossa horta!",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCardHorta(
                        modifier = Modifier.weight(1f),
                        valor = "R$ 4.650",
                        descricao = "arrecadados\neste mês"
                    )
                    StatCardHorta(
                        modifier = Modifier.weight(1f),
                        valor = "128 kg",
                        descricao = "alimentos\ncolhidos"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCardHorta(
                        modifier = Modifier.weight(1f),
                        valor = "56",
                        descricao = "famílias\nbeneficiadas"
                    )
                    StatCardHorta(
                        modifier = Modifier.weight(1f),
                        valor = "23",
                        descricao = "voluntários\nativos"
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Metas da Horta",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                MetaCard(
                    valor = "R$ 120,00",
                    descricao = "Adubo para a horta",
                    status = "Concluída",
                    statusColor = Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(8.dp))

                MetaCard(
                    valor = "R$ 80,00",
                    descricao = "Ferramentas",
                    status = "Concluída",
                    statusColor = Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(8.dp))

                MetaCard(
                    valor = "R$ 60,00",
                    descricao = "Sementes",
                    status = "Em andamento",
                    statusColor = Color(0xFFFF9800)
                )
            }
        }
    }
}

@Composable
fun StatCardHorta(
    modifier: Modifier = Modifier,
    valor: String,
    descricao: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = valor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Text(
                text = descricao,
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun MetaCard(
    valor: String,
    descricao: String,
    status: String,
    statusColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = valor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = descricao,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = statusColor.copy(alpha = 0.2f)
            ) {
                Text(
                    text = status,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = statusColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}