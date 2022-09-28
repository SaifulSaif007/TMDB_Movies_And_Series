package com.saiful.person.di

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
    fun providePersonApi(retrofit: Retrofit): PersonService =
        retrofit.create(PersonService::class.java)
}