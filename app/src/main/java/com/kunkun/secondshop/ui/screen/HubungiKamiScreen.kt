package com.kunkun.secondshop.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Layar Form Hubungi Kami

// Pilihan tipe pesan yang tersedia di dropdown
private val TIPE_PESAN_OPTIONS = listOf("Pertanyaan", "Keluhan", "Saran")

// -------------------------------------------------------------
// STATEFUL Composable — memegang semua state form + launcher foto
// Semua state pakai rememberSaveable agar bertahan saat rotasi layar
// -------------------------------------------------------------
@Composable
fun HubungiKamiScreen(onBack: () -> Unit) {
    // State form — rememberSaveable agar bertahan saat rotasi layar
    var email by rememberSaveable { mutableStateOf("") }
    var tipePesan by rememberSaveable { mutableStateOf(TIPE_PESAN_OPTIONS[0]) }
    var pesan by rememberSaveable { mutableStateOf("") }
    var selectedFileName by rememberSaveable { mutableStateOf<String?>(null) }
    var isAgreed by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Launcher untuk memilih foto dari galeri menggunakan ActivityResultContracts.PickVisualMedia
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            // Ambil nama file dari URI menggunakan ContentResolver
            val cursor = context.contentResolver.query(it, null, null, null, null)
            cursor?.use { c ->
                val nameIndex = c.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0 && c.moveToFirst()) {
                    selectedFileName = c.getString(nameIndex)
                }
            }
            // Fallback jika nama tidak ditemukan
            if (selectedFileName == null) {
                selectedFileName = it.lastPathSegment ?: "foto_terpilih"
            }
        }
    }

    // Teruskan ke Stateless composable (State Hoisting — event naik lewat lambda)
    StatelessFormHubungiKami(
        email = email,
        tipePesan = tipePesan,
        pesan = pesan,
        selectedFileName = selectedFileName,
        isAgreed = isAgreed,
        snackbarHostState = snackbarHostState,
        onEmailChange = { email = it },
        onTipePesanChange = { tipePesan = it },
        onPesanChange = { pesan = it },
        onPickPhotoClick = {
            // Buka photo picker — hanya menampilkan gambar
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onAgreedChange = { isAgreed = it },
        onSendClick = {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("✅ Pesan Terkirim!")
            }
        },
        onBack = onBack
    )
}

// -------------------------------------------------------------
// STATELESS Composable — hanya menerima data + lambda
// Berisi semua elemen UI form tanpa memegang state apapun
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessFormHubungiKami(
    email: String,
    tipePesan: String,
    pesan: String,
    selectedFileName: String?,
    isAgreed: Boolean,
    snackbarHostState: SnackbarHostState,
    onEmailChange: (String) -> Unit,
    onTipePesanChange: (String) -> Unit,
    onPesanChange: (String) -> Unit,
    onPickPhotoClick: () -> Unit,
    onAgreedChange: (Boolean) -> Unit,
    onSendClick: () -> Unit,
    onBack: () -> Unit
) {
    // State lokal untuk dropdown — tidak perlu survive rotasi
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // Validasi form
    val isEmailValid = email.isNotBlank() && email.contains("@")
    val isPesanValid = pesan.length >= 10
    val isFormValid = isEmailValid && isPesanValid && isAgreed

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hubungi Kami",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Kirim pesan kepada tim Kunkun Fashion Store",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // ── Field Email ──────────────────────────────────────────
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange, // event naik ke stateful
                label = { Text("Alamat Email") },
                placeholder = { Text("contoh@email.com") },
                singleLine = true,
                isError = email.isNotBlank() && !isEmailValid,
                supportingText = {
                    if (email.isNotBlank() && !isEmailValid) {
                        Text(
                            text = "Format Email Salah",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                trailingIcon = {
                    if (isEmailValid) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // ── Dropdown Tipe Pesan (ExposedDropdownMenuBox) ─────────
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = tipePesan,
                    onValueChange = {}, // readOnly — tidak bisa diketik langsung
                    readOnly = true,
                    label = { Text("Tipe Pesan") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    TIPE_PESAN_OPTIONS.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onTipePesanChange(option) // event naik ke stateful
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // ── Field Pesan ──────────────────────────────────────────
            OutlinedTextField(
                value = pesan,
                onValueChange = onPesanChange, // event naik ke stateful
                label = { Text("Pesan") },
                placeholder = { Text("Tulis pesanmu di sini...") },
                minLines = 4,
                maxLines = 6,
                isError = pesan.isNotBlank() && !isPesanValid,
                supportingText = {
                    if (pesan.isNotBlank() && !isPesanValid) {
                        Text(
                            text = "Pesan minimal 10 karakter (${pesan.length}/10)",
                            color = MaterialTheme.colorScheme.error
                        )
                    } else if (pesan.isNotBlank()) {
                        Text(
                            text = "${pesan.length} karakter",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // ── Unggah Foto Bukti ────────────────────────────────────
            Column {
                Text(
                    text = "Bukti Foto (Opsional)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onPickPhotoClick, // event naik ke stateful (launcher)
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddPhotoAlternate,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pilih Foto")
                }

                // Tampilkan nama file terpilih dalam Card
                if (selectedFileName != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedFileName,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // ── Checkbox Persetujuan Syarat & Ketentuan ──────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isAgreed,
                    onCheckedChange = onAgreedChange // event naik ke stateful
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Saya menyetujui syarat & ketentuan yang berlaku",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // ── Tombol Kirim Pesan ───────────────────────────────────
            // Enabled hanya jika semua validasi lolos (isFormValid)
            Button(
                onClick = onSendClick, // event naik ke stateful
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Kirim Pesan",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

