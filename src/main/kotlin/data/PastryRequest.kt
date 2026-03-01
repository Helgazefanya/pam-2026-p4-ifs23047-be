package org.delcom.data

import kotlinx.serialization.Serializable
import org.delcom.entities.Pastry

@Serializable
data class PastryRequest(
    var judul: String = "",
    var deskripsi: String = "",
    var bahanUtama: String = "",
    var infoAlergen: String = "",
    var gambar: String = "", // Menggunakan Int untuk ID R.drawable
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "judul" to judul,
            "deskripsi" to deskripsi,
            "bahanUtama" to bahanUtama,
            "infoAlergen" to infoAlergen,
            "gambar" to gambar
        )
    }

    fun toEntity(): Pastry {
        return Pastry(
            judul = judul,
            deskripsi = deskripsi,
            bahanUtama = bahanUtama,
            infoAlergen = infoAlergen,
            gambar = gambar,
        )
    }
}