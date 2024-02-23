package io.github.kmichaelk.unnandroid.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kmichaelk.unnandroid.api.JournalClient
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.api.ScheduleClient
import io.github.kmichaelk.unnandroid.api.SourceClient
import io.github.kmichaelk.unnandroid.api.impl.JournalClientImpl
import io.github.kmichaelk.unnandroid.api.impl.PortalClientImpl
import io.github.kmichaelk.unnandroid.api.impl.ScheduleClientImpl
import io.github.kmichaelk.unnandroid.api.impl.SourceClientImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    @Singleton
    @Binds
    abstract fun bindPortalClient(client: PortalClientImpl): PortalClient

    @Singleton
    @Binds
    abstract fun bindScheduleClient(client: ScheduleClientImpl): ScheduleClient

    @Singleton
    @Binds
    abstract fun bindSourceClient(client: SourceClientImpl): SourceClient

    @Singleton
    @Binds
    abstract fun bindJournalClient(client: JournalClientImpl): JournalClient
}