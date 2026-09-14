package com.leo.clean_mvvm_mvi.core.database.di

import android.content.Context
import androidx.room.Room
import com.leo.clean_mvvm_mvi.core.database.DriverDatabase
import com.leo.clean_mvvm_mvi.core.database.dao.DriverDao
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
    fun provideDriverDatabase(
        @ApplicationContext context: Context
    ): DriverDatabase {
        return Room.databaseBuilder(
            context,
            DriverDatabase::class.java,
            "driver-database"
        ).build()
    }

    @Provides
    fun provideDriverDao(database: DriverDatabase): DriverDao {
        return database.driverDao()
    }
}

