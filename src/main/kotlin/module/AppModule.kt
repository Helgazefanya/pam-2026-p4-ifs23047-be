package org.delcom.module

import org.delcom.repositories.IPlantRepository
import org.delcom.repositories.PlantRepository
import org.delcom.services.PlantService
import org.delcom.repositories.IPastryRepository
import org.delcom.repositories.PastryRepository
import org.delcom.services.PastryService // Pastikan Service-nya juga di-import
import org.delcom.services.ProfileService
import org.koin.dsl.module


val appModule = module {
    // Plant Repository
    single<IPlantRepository> {
        PlantRepository()
    }
    single<IPastryRepository> { PastryRepository() }


    // Plant Service
    single {
        PlantService(get())
    }
    single { PastryService(get()) }

    // Profile Service
    single {
        ProfileService()
    }
}