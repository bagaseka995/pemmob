package com.kunkun.secondshop.data.model

// Model data untuk produk
data class Product(
    val id: Int,
    val category_id: Int,
    val category: Category? = null, // diisi oleh ViewModel, bukan dari JSON
    val name: String,
    val description: String?,
    val price: Double,
    val img: String,
    val size_stocks: Map<String, Int> = emptyMap(), // key = ukuran, value = stok
    val brand: String?
) {
    // Total stok semua ukuran
    val stock: Int get() = size_stocks.values.sum()

    // Daftar ukuran
    val sizes: List<String> get() = size_stocks.keys.toList()
}
