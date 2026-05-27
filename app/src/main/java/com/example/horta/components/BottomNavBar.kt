package com.example.horta.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horta.R

// Itens do menu
data class BottomNavItem(
    val route: String,
    val icon: Int,
    val label: String
)

val menuItems = listOf(
    BottomNavItem("home", R.drawable.homeicon, "home"),
    BottomNavItem("horta", R.drawable.hortaicon, "Horta"),
    BottomNavItem("loja", R.drawable.lojaicon, "Loja"),
    BottomNavItem("doacao", R.drawable.doaricon, "Doar"),
    BottomNavItem("perfil", R.drawable.perfilicon, "Perfil")
)

@Composable
fun BottomNavBar(
    currentRoute: String,
    onItemSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF4CAF50),
        tonalElevation = 10.dp
    ) {
        menuItems.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = androidx.compose.ui.Modifier.size(24.dp),
                        tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f)
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
                    indicatorColor = Color(0xFF2E7D32)
                )
            )
        }
    }
}