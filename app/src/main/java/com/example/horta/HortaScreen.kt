package com.example.horta.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Impacto da comunidade",
                            style = DinamicTypography.displaySmall,
                            color = CinzaEscuro
                        )
                        Text(
                            text = "Veja como sua ajuda transforma vidas e a nossa horta!",
                            style = DinamicTypography.bodyMedium,
                            color = CinzaClaro
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
                    style = DinamicTypography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = VerdeEscuro,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                MetaCard(
                    valor = "R$ 120,00",
                    descricao = "Adubo para a horta",
                    status = "Concluída",
                    statusColor = VerdePrincipal
                )

                Spacer(modifier = Modifier.height(8.dp))

                MetaCard(
                    valor = "R$ 80,00",
                    descricao = "Ferramentas",
                    status = "Concluída",
                    statusColor = VerdePrincipal
                )

                Spacer(modifier = Modifier.height(8.dp))

                MetaCard(
                    valor = "R$ 60,00",
                    descricao = "Sementes",
                    status = "Em andamento",
                    statusColor = AmareloDestaque
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
        modifier = modifier
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp), clip = true),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
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
                style = DinamicTypography.bodySmall,
                color = VerdeEscuro
            )
            Text(
                text = descricao,
                style = DinamicTypography.bodySmall,
                color = CinzaClaro,
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
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp), clip = true),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
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
                    style = DinamicTypography.bodySmall,
                    color = VerdeEscuro
                )
                Text(
                    text = descricao,
                    style = DinamicTypography.bodySmall,
                    color = CinzaEscuro
                )
            }
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = statusColor.copy(alpha = 0.2f)
            ) {
                Text(
                    text = status,
                    style = DinamicTypography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = statusColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}