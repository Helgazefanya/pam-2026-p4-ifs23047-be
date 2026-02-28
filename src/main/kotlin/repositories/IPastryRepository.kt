package org.delcom.repositories

import org.delcom.entities.Pastry

interface IPastryRepository {
    suspend fun getPastries(search: String): List<Pastry>
    suspend fun getPastryById(id: String): Pastry?
    suspend fun getPastryByTitle(title: String): Pastry?
    suspend fun addPastry(pastry: Pastry): String
    suspend fun updatePastry(id: String, newPastry: Pastry): Boolean
    suspend fun removePastry(id: String): Boolean
}