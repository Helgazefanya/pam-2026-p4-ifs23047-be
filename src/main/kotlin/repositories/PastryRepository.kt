package org.delcom.repositories

import org.delcom.dao.PastryDAO
import org.delcom.entities.Pastry
import org.delcom.helpers.daoToModel
import org.delcom.helpers.suspendTransaction
import org.delcom.tables.PastryTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.lowerCase
import java.util.UUID

class PastryRepository : IPastryRepository {
    override suspend fun getPastries(search: String): List<Pastry> = suspendTransaction {
        if (search.isBlank()) {
            PastryDAO.all()
                .orderBy(PastryTable.createdAt to SortOrder.DESC)
                .limit(20)
                .map(::daoToModel)
        } else {
            val keyword = "%${search.lowercase()}%"

            PastryDAO
                .find {
                    PastryTable.judul.lowerCase() like keyword
                }
                .orderBy(PastryTable.judul to SortOrder.ASC)
                .limit(20)
                .map(::daoToModel)
        }
    }

    override suspend fun getPastryById(id: String): Pastry? = suspendTransaction {
        PastryDAO
            .find { (PastryTable.id eq UUID.fromString(id)) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun getPastryByTitle(title: String): Pastry? = suspendTransaction {
        PastryDAO
            .find { (PastryTable.judul eq title) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun addPastry(pastry: Pastry): String = suspendTransaction {
        val pastryDAO = PastryDAO.new {
            judul = pastry.judul
            gambar = pastry.gambar
            deskripsi = pastry.deskripsi
            bahanUtama = pastry.bahanUtama
            infoAlergen = pastry.infoAlergen
            createdAt = pastry.createdAt
            updatedAt = pastry.updatedAt
        }

        pastryDAO.id.value.toString()
    }

    override suspend fun updatePastry(id: String, newPastry: Pastry): Boolean = suspendTransaction {
        val pastryDAO = PastryDAO
            .find { PastryTable.id eq UUID.fromString(id) }
            .limit(1)
            .firstOrNull()

        if (pastryDAO != null) {
            pastryDAO.judul = newPastry.judul
            pastryDAO.gambar = newPastry.gambar
            pastryDAO.deskripsi = newPastry.deskripsi
            pastryDAO.bahanUtama = newPastry.bahanUtama
            pastryDAO.infoAlergen = newPastry.infoAlergen
            pastryDAO.updatedAt = newPastry.updatedAt
            true
        } else {
            false
        }
    }

    override suspend fun removePastry(id: String): Boolean = suspendTransaction {
        val rowsDeleted = PastryTable.deleteWhere {
            PastryTable.id eq UUID.fromString(id)
        }
        rowsDeleted == 1
    }
}