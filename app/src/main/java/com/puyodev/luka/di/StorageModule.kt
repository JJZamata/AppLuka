package com.puyodev.luka.di

import com.google.firebase.firestore.FirebaseFirestore
import com.puyodev.luka.model.service.AccountService
import com.puyodev.luka.model.service.StorageService
import com.puyodev.luka.model.service.impl.StorageServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module//Marca la clase como un modulo de Dagger donde se declaran las independencias
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides//Indica que el metodo proporciona una instancia de un tipo especifico
    @Singleton//Define que la instancia proporcionada sera unica en toda la aplicacion
    fun provideStorageService(
        firestore: FirebaseFirestore,
        auth: AccountService
    ): StorageService {
        return StorageServiceImpl(firestore, auth)
    }
}