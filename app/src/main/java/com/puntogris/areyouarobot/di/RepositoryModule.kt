package com.puntogris.areyouarobot.di

import com.puntogris.areyouarobot.data.IRepository
import com.puntogris.areyouarobot.data.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repository: Repository): IRepository
}