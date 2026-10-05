package com.kunkun.secondshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunkun.secondshop.data.model.Category
import com.kunkun.secondshop.data.model.Product
import com.kunkun.secondshop.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State untuk UI Produk
sealed interface ProductUiState {
    object Loading : ProductUiState
    data class Success(
        val categories: List<Category>,
        val products: List<Product>
    ) : ProductUiState
    data class Error(val message: String) : ProductUiState
}

// ViewModel untuk mengelola data produk dan kategori
class ProductViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    // Mengambil data kategori dan produk dari API
    private fun fetchData() {
        _uiState.value = ProductUiState.Loading

        viewModelScope.launch {
            try {
                val categories  = ApiClient.instance.getCategories()
                val rawProducts = ApiClient.instance.getProducts()

                // Pasangkan kategori ke masing-masing produk
                val products = rawProducts.map { product ->
                    val matchedCategory = categories.find { it.id == product.category_id }
                    product.copy(category = matchedCategory)
                }

                _uiState.value = ProductUiState.Success(
                    categories = categories,
                    products   = products
                )
            } catch (e: Exception) {
                _uiState.value = ProductUiState.Error(
                    message = "Gagal memuat data: ${e.localizedMessage}"
                )
            }
        }
    }

    // Mengurangi stok produk saat checkout
    fun reduceStock(productId: Int, size: String, quantity: Int) {
        val currentState = _uiState.value
        if (currentState !is ProductUiState.Success) return

        val updatedProducts = currentState.products.map { product ->
            if (product.id == productId) {
                val updatedSizeStocks = product.size_stocks.toMutableMap()
                val currentSizeStock  = updatedSizeStocks[size] ?: 0
                updatedSizeStocks[size] = (currentSizeStock - quantity).coerceAtLeast(0)
                product.copy(size_stocks = updatedSizeStocks)
            } else product
        }

        _uiState.value = currentState.copy(products = updatedProducts)
    }

    // Memuat ulang data
    fun retry() {
        fetchData()
    }
}
