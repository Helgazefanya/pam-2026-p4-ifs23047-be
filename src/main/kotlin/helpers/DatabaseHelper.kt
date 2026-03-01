package org.delcom.helpers

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.delcom.tables.PlantTable
import org.delcom.tables.PastryTable

fun Application.configureDatabases() {
    // Mengambil data dari System Property (yang diisi oleh .env di Application.kt)
    val dbHost = System.getProperty("DB_HOST") ?: "127.0.0.1"
    val dbPort = System.getProperty("DB_PORT") ?: "5432"
    val dbName = System.getProperty("DB_NAME") ?: "db_pam_pastry"
    val dbUser = System.getProperty("DB_USER") ?: "postgres"
    val dbPassword = System.getProperty("DB_PASSWORD") ?: "postgres"

    // Melakukan koneksi ke Database
    Database.connect(
        url = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )

    // Fitur "Tukang Bangunan": Otomatis membuat tabel jika belum ada di database
    transaction {
        SchemaUtils.create(PlantTable, PastryTable)
    }
}