package io.github.kmichaelk.unnandroid.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {

    @Provides
    @Named("master_key")
    @Singleton
    fun provideMasterKey(): String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    @Provides
    @Named("auth")
    @Singleton
    fun provideAuthPreferences(
        @ApplicationContext context: Context,
        @Named("master_key") masterKey: String
    ): SharedPreferences = EncryptedSharedPreferences.create(
        "auth",
        masterKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    @Provides
    @Named("schedule")
    @Singleton
    fun provideSchedulePreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences("schedule", Context.MODE_PRIVATE)
}