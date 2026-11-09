package com.saiful.shared.di

import com.saiful.shared.data.api.AccountApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountModule {

    @Provides
    @Singleton
    fun provideAccountApiService(retrofit: Retrofit): AccountApiService =
        retrofit.create(AccountApiService::class.java)
}