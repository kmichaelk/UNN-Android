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

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kmichaelk.unnandroid.network.NetworkConnectionInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Named("base")
    @Singleton
    fun provideBaseOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient = OkHttpClient.Builder()
        .protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(NetworkConnectionInterceptor(context))
        //.addInterceptor(okhttp3.logging.HttpLoggingInterceptor().apply {
        //    setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY)
        //})
        .build()

    @Provides
    @Named("caching")
    @Singleton
    fun provideCachingOkHttpClient(
        @ApplicationContext appContext: Context,
        @Named("base") client: OkHttpClient
    ): OkHttpClient = client.newBuilder()
        .cache(
            Cache(
                directory = File(appContext.cacheDir, "http_cache"),
                maxSize = 10L * 1024L * 1024L // 10MB
            )
        )
        .build()
}