package org.delcom.dao

import org.delcom.tables.PastryTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import java.util.UUID

class PastryDAO(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, PastryDAO>(PastryTable)

    var judul by PastryTable.judul
    var gambar by PastryTable.gambar // Menggunakan Int untuk ID R.drawable
    var deskripsi by PastryTable.deskripsi
    var bahanUtama by PastryTable.bahanUtama
    var infoAlergen by PastryTable.infoAlergen
    var createdAt by PastryTable.createdAt
    var updatedAt by PastryTable.updatedAt
}