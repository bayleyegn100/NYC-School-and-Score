package com.yedebkid.jpmccodingchalleng.di

import com.yedebkid.jpmccodingchalleng.rest.SchoolsRepository
import com.yedebkid.jpmccodingchalleng.rest.SchoolsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesRepository(
        schoolsRepositoryImpl: SchoolsRepositoryImpl
    ): SchoolsRepository
}