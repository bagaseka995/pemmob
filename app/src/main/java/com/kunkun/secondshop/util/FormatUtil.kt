package com.kunkun.secondshop.util

import java.text.NumberFormat
import java.util.Locale

// Utilitas format angka rupiah untuk tampilan UI
object FormatUtil {
    private val localeID = Locale("id", "ID")

    fun formatRupiah(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(localeID)
        return "Rp ${formatter.format(amount.toLong())}"
    }
}
