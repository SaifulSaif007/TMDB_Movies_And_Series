package com.saiful.person.di

import com.saiful.base.util.navigation.PersonModuleNavigation
import com.saiful.person.data.api.PersonApiService
import com.saiful.person.navigation.NavigationImpl
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

    @Provides
    @Singleton
    fun provideNavigation(): PersonModuleNavigation = NavigationImpl()

}