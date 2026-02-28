package org.delcom.helpers

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.PlantDAO
import org.delcom.dao.PastryDAO // Tambahkan import ini
import org.delcom.entities.Plant
import org.delcom.entities.Pastry // Tambahkan import ini
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

// Helper untuk Plant
fun daoToModel(dao: PlantDAO) = Plant(
    dao.id.value.toString(),
    dao.nama,
    dao.pathGambar,
    dao.deskripsi,
    dao.manfaat,
    dao.efekSamping,
    dao.createdAt,
    dao.updatedAt
)

// TAMBAHKAN INI: Helper untuk Pastry
fun daoToModel(dao: PastryDAO) = Pastry(
    dao.id.value.toString(),
    dao.judul,
    dao.gambar,
    dao.deskripsi,
    dao.bahanUtama,
    dao.infoAlergen,
    dao.createdAt,
    dao.updatedAt
)