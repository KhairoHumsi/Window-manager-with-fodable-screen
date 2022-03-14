package com.khairo.windowmanagerwithfodablescreen.data.di.modules

import com.khairo.windowmanagerwithfodablescreen.data.di.impl.MainRepositoryImpl
import com.khairo.windowmanagerwithfodablescreen.data.repositories.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class MainRepositoryModules {

    @Binds
    abstract fun providesModule(homeRepository: MainRepositoryImpl): MainRepository

}
