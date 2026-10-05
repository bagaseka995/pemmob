package com.kunkun.secondshop.data.model

// Model data untuk kategori produk
data class Category(
    val id: Int,
    val name: String,
    val description: String?,
    val products_count: Int?
)
