package org.delcom.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Pastry(
    var id : String = UUID.randomUUID().toString(),
    var judul: String,
    var gambar: Int, // Digunakan untuk R.drawable.nama_roti
    var deskripsi: String,
    var bahanUtama: String,
    var infoAlergen: String,

    @Contextual
    val createdAt: Instant = Clock.System.now(),
    @Contextual
    var updatedAt: Instant = Clock.System.now(),
)