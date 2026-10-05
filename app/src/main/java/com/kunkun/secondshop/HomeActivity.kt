package com.kunkun.secondshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kunkun.secondshop.ui.screen.DaftarProdukScreen
import com.kunkun.secondshop.ui.screen.DetailProdukScreen
import com.kunkun.secondshop.ui.screen.HubungiKamiScreen
import com.kunkun.secondshop.ui.theme.KunkunSecondShopTheme
import com.kunkun.secondshop.ui.viewmodel.ProductViewModel

// Activity utama aplikasi
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KunkunSecondShopTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KunkunApp()
                }
            }
        }
    }
}

// Navigasi aplikasi dengan NavHost
@Composable
fun KunkunApp(
    productViewModel: ProductViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "daftar_produk"
    ) {
        // Layar Daftar Produk (halaman utama)
        composable(route = "daftar_produk") {
            DaftarProdukScreen(
                productViewModel = productViewModel,
                onProductClick = { productId ->
                    navController.navigate("detail/$productId")
                },
                onHubungiKamiClick = {
                    navController.navigate("hubungi_kami")
                }
            )
        }

        // Layar Detail Produk
        composable(
            route = "detail/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
            DetailProdukScreen(
                productId = productId,
                productViewModel = productViewModel,
                onBack = { navController.navigateUp() }
            )
        }

        // Layar Hubungi Kami
        composable(route = "hubungi_kami") {
            HubungiKamiScreen(
                onBack = { navController.navigateUp() }
            )
        }
    }
}
