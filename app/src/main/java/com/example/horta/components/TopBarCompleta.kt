package com.example.horta.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.R
import com.example.horta.ui.theme.DinamicTypography

@Composable
fun TopBarCompleta(
    titulo: String,
    onVoltarClick: () -> Unit,
    onLimparClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onVoltarClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.voltaricon),
                contentDescription = "voltar",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = titulo,
            style = DinamicTypography.displaySmall,
            color = Color(0xFF2E7D32)
        )

        IconButton(
            onClick = onLimparClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.lixoicon),
                contentDescription = "Limpar carrinho",
                tint = Color(0xFFF44336),
                modifier = Modifier.size(34.dp)
            )
        }
    }
}