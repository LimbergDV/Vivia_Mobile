package com.limbergdv.vivia_mobile.core.di

import android.content.Context
import androidx.room.Room
import com.limbergdv.vivia_mobile.core.database.AppDatabase
import com.limbergdv.vivia_mobile.core.database.dao.PendingImageDao
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDao
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDraftDao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ViviaDB"
        )
            .addMigrations(
                AppDatabase.MIGRATION_1_2,
                AppDatabase.MIGRATION_2_3,
                AppDatabase.MIGRATION_3_4
            )
            .build()
    }

    @Provides
    fun providePropertyDraftDao(db: AppDatabase): PropertyDraftDao = db.propertyDraftDao()

    @Provides
    fun providePropertyDao(db: AppDatabase): PropertyDao = db.propertyDao()

    @Provides
    fun providePendingImageDao(db: AppDatabase): PendingImageDao = db.pendingImageDao()
}