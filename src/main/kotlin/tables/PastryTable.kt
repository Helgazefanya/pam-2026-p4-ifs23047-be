package org.delcom.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object PastryTable : UUIDTable("pastries") {
    val judul = varchar("judul", 255)
    val gambar = integer("gambar") // Menyimpan resource ID (Int) dari R.drawable
    val deskripsi = text("deskripsi")
    val bahanUtama = text("bahan_utama")
    val infoAlergen = text("info_alergen")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}