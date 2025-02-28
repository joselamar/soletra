package com.developer.lamarao.soletra.di

import android.content.Context
import androidx.room.Room
import com.developer.lamarao.soletra.database.SoletraDatabase
import com.developer.lamarao.soletra.database.dao.GameDao
import com.developer.lamarao.soletra.database.dao.PangramsDao
import com.developer.lamarao.soletra.database.dao.WordsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideSoletraDatabase(@ApplicationContext context: Context): SoletraDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            SoletraDatabase::class.java,
            "soletra_database.db"
        ).createFromAsset("database/soletra_database.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideGameDao(soletraDatabase: SoletraDatabase): GameDao = soletraDatabase.gameDao

    @Singleton
    @Provides
    fun provideWordsDao(soletraDatabase: SoletraDatabase): WordsDao = soletraDatabase.wordsDao

    @Singleton
    @Provides
    fun providePangramsDao(soletraDatabase: SoletraDatabase): PangramsDao = soletraDatabase.pangramsDao

}