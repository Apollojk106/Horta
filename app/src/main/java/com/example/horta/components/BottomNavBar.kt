package com.example.horta.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Itens do menu
data class BottomNavItem(
    val route: String,
    val icon: String,
    val label: String
)

val menuItems = listOf(
    BottomNavItem("inicio", "🏠", "Início"),
    BottomNavItem("horta", "🌱", "Horta"),
    BottomNavItem("loja", "🛒", "Loja"),
    BottomNavItem("doacao", "🎁", "Doar"),
    BottomNavItem("perfil", "👤", "Perfil")
)

@Composable
fun BottomNavBar(
    currentRoute: String,  // Tela atual (ex: "horta", "loja", etc)
    onItemSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF4CAF50),  // Fundo verde
        tonalElevation = 8.dp
    ) {
        menuItems.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item.route) },
                icon = {
                    Text(
                        text = item.icon,
                        fontSize = 24.sp,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.5f),
                    unselectedTextColor = Color.White.copy(alpha = 0.5f),
                    indicatorColor = Color(0xFF2E7D32)  // Cor de fundo do item selecionado
                )
            )
        }
    }
}