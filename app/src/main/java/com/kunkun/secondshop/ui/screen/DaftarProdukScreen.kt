package com.kunkun.secondshop.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kunkun.secondshop.data.model.Category
import com.kunkun.secondshop.data.model.Product
import com.kunkun.secondshop.ui.theme.Teal40
import com.kunkun.secondshop.ui.viewmodel.ProductUiState
import com.kunkun.secondshop.ui.viewmodel.ProductViewModel
import com.kunkun.secondshop.util.FormatUtil

// Layar Utama Daftar Produk

// Peta ikon per nama kategori
private fun categoryIcon(name: String): ImageVector = when {
    name.contains("Atasan", ignoreCase = true)    -> Icons.Filled.Checkroom
    name.contains("Bawahan", ignoreCase = true)   -> Icons.Filled.Style
    name.contains("Outer", ignoreCase = true)     -> Icons.Filled.Diamond
    name.contains("Aksesoris", ignoreCase = true) -> Icons.Filled.Watch
    else                                          -> Icons.Filled.ShoppingCart
}

// -------------------------------------------------------------
// STATEFUL Composable
// -------------------------------------------------------------
@Composable
fun DaftarProdukScreen(
    productViewModel: ProductViewModel,
    onProductClick: (Int) -> Unit,
    onHubungiKamiClick: () -> Unit
) {
    val uiState by productViewModel.uiState.collectAsState()

    var selectedCategoryId by rememberSaveable { mutableStateOf<Int?>(null) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is ProductUiState.Success && selectedCategoryId == null) {
            selectedCategoryId = (uiState as ProductUiState.Success).categories.firstOrNull()?.id
        }
    }

    StatelessDaftarProduk(
        uiState = uiState,
        selectedCategoryId = selectedCategoryId,
        searchQuery = searchQuery,
        onCategorySelected = { id -> selectedCategoryId = id },
        onSearchQueryChange = { query -> searchQuery = query },
        onProductClick = onProductClick,
        onHubungiKamiClick = onHubungiKamiClick,
        onRetry = { productViewModel.retry() }
    )
}

// -------------------------------------------------------------
// STATELESS Composable
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessDaftarProduk(
    uiState: ProductUiState,
    selectedCategoryId: Int?,
    searchQuery: String,
    onCategorySelected: (Int) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onProductClick: (Int) -> Unit,
    onHubungiKamiClick: () -> Unit,
    onRetry: () -> Unit
) {
    var showDropdownMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kunkun Fashion Store",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    // Menu overflow
                    Box {
                        IconButton(onClick = { showDropdownMenu = true }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Menu Lainnya"
                            )
                        }
                        DropdownMenu(
                            expanded = showDropdownMenu,
                            onDismissRequest = { showDropdownMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Hubungi Kami") },
                                onClick = {
                                    showDropdownMenu = false
                                    onHubungiKamiClick()
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                // State Loading
                is ProductUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Memuat produk...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // State Error
                is ProductUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "😕 Oops!",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onRetry) {
                                Text("Coba Lagi")
                            }
                        }
                    }
                }

                // State Success
                is ProductUiState.Success -> {
                    val filteredProducts = state.products.filter { product ->
                        val matchesCategory = selectedCategoryId == null ||
                                product.category_id == selectedCategoryId
                        val matchesSearch = searchQuery.isBlank() ||
                                product.name.contains(searchQuery, ignoreCase = true) ||
                                product.brand?.contains(searchQuery, ignoreCase = true) == true
                        matchesCategory && matchesSearch
                    }

                    // ── Search bar modern ────────────────────────────
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = {
                            Text(
                                "Cari produk fashion...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Cari",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // ── Kategori dengan ikon ─────────────────────────
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        items(state.categories, key = { it.id }) { category ->
                            CategoryIconItem(
                                category = category,
                                isSelected = category.id == selectedCategoryId,
                                onCategoryClick = { onCategorySelected(category.id) }
                            )
                        }
                    }

                    // ── Label Daftar Produk ──────────────────────────
                    Text(
                        text = "Daftar Produk",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    if (filteredProducts.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "😔 Produk tidak ditemukan.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        // ── List produk vertikal ─────────────────────
                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = 16.dp, end = 16.dp, bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(filteredProducts, key = { it.id }) { product ->
                                ProductListCard(
                                    product = product,
                                    onClick = { onProductClick(product.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Card produk horizontal
@Composable
fun ProductListCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Info produk (kiri) ───────────────────────────
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Nama produk
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Deskripsi singkat
                product.description?.let { desc ->
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Harga
                Text(
                    text = FormatUtil.formatRupiah(product.price),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

            }

            Spacer(modifier = Modifier.width(12.dp))

            // ── Gambar produk (kanan) ────────────────────────
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                AsyncImage(
                    model = product.img,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Badge kategori di pojok kanan atas gambar
                product.category?.let { cat ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp),
                        color = Teal40.copy(alpha = 0.92f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = cat.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                            fontSize = 9.sp
                        )
                    }
                }
                // Overlay TERJUAL
                if (product.stock == 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                                ),
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "TERJUAL",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Komponen ikon kategori
@Composable
fun CategoryIconItem(
    category: Category,
    isSelected: Boolean,
    onCategoryClick: () -> Unit
) {
    val icon = categoryIcon(category.name)

    Column(
        modifier = Modifier
            .clickable(onClick = onCategoryClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Lingkaran ikon
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = category.name,
                tint = if (isSelected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(26.dp)
            )
        }
        // Nama kategori
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
        // Jumlah item
        category.products_count?.let { count ->
            Text(
                text = "$count item",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

