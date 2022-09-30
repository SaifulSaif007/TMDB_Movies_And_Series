package com.saiful.person.di

import com.saiful.person.data.api.PersonApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersonService {

    @Provides
    @Singleton
    fun providePersonApi(retrofit: Retrofit): PersonApiService =
        retrofit.create(PersonApiService::class.java)
}