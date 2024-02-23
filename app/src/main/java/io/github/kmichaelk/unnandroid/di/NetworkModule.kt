package io.github.kmichaelk.unnandroid.di

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kmichaelk.unnandroid.network.NetworkConnectionInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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