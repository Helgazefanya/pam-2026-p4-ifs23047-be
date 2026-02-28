package org.delcom.services

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import org.delcom.data.AppException
import org.delcom.data.DataResponse
import org.delcom.data.PastryRequest
import org.delcom.helpers.ValidatorHelper
import org.delcom.repositories.IPastryRepository
import java.io.File
import java.util.*

class PastryService(private val pastryRepository: IPastryRepository) {

    // Mengambil semua data pastry
    suspend fun getAllPastries(call: ApplicationCall) {
        val search = call.request.queryParameters["search"] ?: ""

        val pastries = pastryRepository.getPastries(search)

        val response = DataResponse(
            "success",
            "Berhasil mengambil daftar pastry",
            mapOf(Pair("pastries", pastries))
        )
        call.respond(response)
    }

    // Mengambil data pastry berdasarkan id
    suspend fun getPastryById(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID pastry tidak boleh kosong!")

        val pastry = pastryRepository.getPastryById(id) ?: throw AppException(404, "Data pastry tidak tersedia!")

        val response = DataResponse(
            "success",
            "Berhasil mengambil data pastry",
            mapOf(Pair("pastry", pastry))
        )
        call.respond(response)
    }

    // Ambil data request (Multipart Form Data)
    private suspend fun getPastryRequest(call: ApplicationCall): PastryRequest {
        val pastryReq = PastryRequest()

        val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 5)
        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "judul" -> pastryReq.judul = part.value.trim()
                        "deskripsi" -> pastryReq.deskripsi = part.value
                        "bahanUtama" -> pastryReq.bahanUtama = part.value
                        "infoAlergen" -> pastryReq.infoAlergen = part.value
                    }
                }

                is PartData.FileItem -> {
                    val ext = part.originalFileName
                        ?.substringAfterLast('.', "")
                        ?.let { if (it.isNotEmpty()) ".$it" else "" }
                        ?: ""

                    val fileName = UUID.randomUUID().toString() + ext
                    val filePath = "uploads/pastries/$fileName"

                    val file = File(filePath)
                    file.parentFile.mkdirs()

                    part.provider().copyAndClose(file.writeChannel())
                    // Jika di database/entity field gambarnya String (path), gunakan ini:
                    // pastryReq.gambarPath = filePath
                }
                else -> {}
            }
            part.dispose()
        }
        return pastryReq
    }

    // Validasi request data
    private fun validatePastryRequest(pastryReq: PastryRequest){
        val validatorHelper = ValidatorHelper(pastryReq.toMap())
        validatorHelper.required("judul", "Judul tidak boleh kosong")
        validatorHelper.required("deskripsi", "Deskripsi tidak boleh kosong")
        validatorHelper.required("bahanUtama", "Bahan utama tidak boleh kosong")
        validatorHelper.required("infoAlergen", "Info alergen tidak boleh kosong")
        // Catatan: Jika menggunakan upload file, validasi path file-nya di sini
        validatorHelper.validate()
    }

    // Menambahkan data pastry
    suspend fun createPastry(call: ApplicationCall) {
        val pastryReq = getPastryRequest(call)
        validatePastryRequest(pastryReq)

        // Periksa pastry dengan judul yang sama
        val existPastry = pastryRepository.getPastryByTitle(pastryReq.judul)
        if(existPastry != null){
            throw AppException(409, "Pastry dengan judul ini sudah terdaftar!")
        }

        val pastryId = pastryRepository.addPastry(pastryReq.toEntity())

        val response = DataResponse(
            "success",
            "Berhasil menambahkan data pastry",
            mapOf(Pair("pastryId", pastryId))
        )
        call.respond(response)
    }

    // Mengubah data pastry
    suspend fun updatePastry(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID pastry tidak boleh kosong!")

        val oldPastry = pastryRepository.getPastryById(id) ?: throw AppException(404, "Data pastry tidak tersedia!")

        val pastryReq = getPastryRequest(call)

        // Jika gambar tidak diupload ulang, gunakan gambar lama
        // if(pastryReq.gambarPath.isEmpty()) pastryReq.gambarPath = oldPastry.gambarPath

        validatePastryRequest(pastryReq)

        if(pastryReq.judul != oldPastry.judul){
            val existPastry = pastryRepository.getPastryByTitle(pastryReq.judul)
            if(existPastry != null) throw AppException(409, "Judul pastry sudah digunakan!")
        }

        val isUpdated = pastryRepository.updatePastry(id, pastryReq.toEntity())
        if (!isUpdated) throw AppException(400, "Gagal memperbarui data pastry!")

        call.respond(DataResponse("success", "Berhasil mengubah data pastry", null))
    }

    // Menghapus data pastry
    suspend fun deletePastry(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID pastry tidak boleh kosong!")

        val pastry = pastryRepository.getPastryById(id) ?: throw AppException(404, "Data pastry tidak ditemukan!")

        val isDeleted = pastryRepository.removePastry(id)
        if (!isDeleted) throw AppException(400, "Gagal menghapus data pastry!")

        // Logika hapus file fisik jika ada path gambar
        // val file = File(pastry.gambarPath)
        // if(file.exists()) file.delete()

        call.respond(DataResponse("success", "Berhasil menghapus data pastry", null))
    }

    // Mengambil file gambar pastry
    suspend fun getPastryImage(call: ApplicationCall) {
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest)
        val pastry = pastryRepository.getPastryById(id) ?: return call.respond(HttpStatusCode.NotFound)

        // val file = File(pastry.gambarPath)
        // if (!file.exists()) return call.respond(HttpStatusCode.NotFound)
        // call.respondFile(file)
    }
}