package com.example.pomaryapp.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.pomaryapp.core.utils.Constants
import com.example.pomaryapp.data.local.PomaryDatabase
import com.example.pomaryapp.data.local.dao.OrderDao
import com.example.pomaryapp.data.local.dao.PreorderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PomaryDatabase {
        return Room.databaseBuilder(
            context,
            PomaryDatabase::class.java,
            Constants.DATABASE_NAME
        ) .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun providePreorderDao(db: PomaryDatabase): PreorderDao = db.preorderDao()

    @Provides
    fun provideOrderDao(db: PomaryDatabase): OrderDao = db.orderDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(Constants.DATASTORE_NAME) }
        )
    }
}
