/*
 * This file is part of UNN-Android.
 *
 * UNN-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UNN-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UNN-Android.  If not, see <https://www.gnu.org/licenses/>.
 */

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