package com.example.horta

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.horta.database.DatabaseHelper
import com.example.horta.loja.*
import com.example.horta.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        setContent {
            HortaTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var telaAtual by remember { mutableStateOf("inicio") }
    val context = LocalContext.current

    when (telaAtual) {
        // Tela inicial
        "inicio" -> InicioScreen(
            onComecarClick = { telaAtual = "login" }
        )

        // Login e autenticação
        "login" -> LoginScreen(
            onLoginSuccess = {
                Toast.makeText(context, "Login realizado!", Toast.LENGTH_LONG).show()
                telaAtual = "horta"
            },
            onCadastroClick = { telaAtual = "cadastro" },
            onEsqueciSenhaClick = { telaAtual = "recuperarSenha" }
        )

        "cadastro" -> CadastroScreen(
            onCadastroSuccess = {
                Toast.makeText(context, "Cadastro realizado!", Toast.LENGTH_LONG).show()
                telaAtual = "login"
            },
            onVoltarClick = { telaAtual = "login" }
        )

        "recuperarSenha" -> RecuperarSenhaScreen(
            onVoltarClick = { telaAtual = "login" }
        )

        "horta" -> HortaScreen(
            onNavigateTo = { telaAtual = it },
            onLogout = {
                Toast.makeText(context, "Logout realizado!", Toast.LENGTH_SHORT).show()
                telaAtual = "inicio"
            }
        )

        "doacao" -> DoacaoScreen(
            onNavigateTo = { telaAtual = it },
            onVoltar = { telaAtual = "horta" },
        )

        "pedidos" -> PedidosScreen(
            onNavigateTo = { telaAtual = it }
        )

        "perfil" -> PerfilScreen(
            onNavigateTo = { telaAtual = it },
            onLogout = {
                Toast.makeText(context, "Logout realizado!", Toast.LENGTH_SHORT).show()
                telaAtual = "inicio"
            }
        )

        // Fluxo de compras
        "loja" -> LojaScreen(
            onNavigateTo = { telaAtual = it },
            onVerCarrinho = { telaAtual = "carrinho" },
            onLogout = {
                Toast.makeText(context, "Logout realizado!", Toast.LENGTH_SHORT).show()
                telaAtual = "inicio"
            },
            onVerHorta = { telaAtual = "horta" },
            onVerDoacao = { telaAtual = "doacao" }
        )

        "carrinho" -> CarrinhoScreen(
            onNavigateTo = { telaAtual = it },
            onFinalizar = { telaAtual = "entrega" },
            onVoltar = { telaAtual = "loja" }
        )

        "entrega" -> EntregaScreen(
            onNavigateTo = { telaAtual = it },
            onProximo = { telaAtual = "pagamento" },
            onVoltar = { telaAtual = "carrinho" }
        )

        "pagamento" -> PagamentoScreen(
            onNavigateTo = { telaAtual = it },
            onConfirmar = { telaAtual = "qrcode" },
            onVoltar = { telaAtual = "entrega" }
        )

        "qrcode" -> QRcodeScreen(
            onNavigateTo = { telaAtual = it },
            onConcluir = {
                Toast.makeText(context, "Compra finalizada!", Toast.LENGTH_LONG).show()
                telaAtual = "loja"
            }
        )
    }
}