package com.developer.lamarao.soletra.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.developer.lamarao.soletra.database.converter.DataTypeConverters
import com.developer.lamarao.soletra.database.dao.GameDao
import com.developer.lamarao.soletra.database.dao.PangramsDao
import com.developer.lamarao.soletra.database.dao.WordsDao
import com.developer.lamarao.soletra.database.model.Game
import com.developer.lamarao.soletra.database.model.Pangram
import com.developer.lamarao.soletra.database.model.Words

@Database(entities = [Game::class, Words::class, Pangram::class], version = 1)
@TypeConverters(DataTypeConverters::class)
abstract class SoletraDatabase : RoomDatabase() {

    abstract val gameDao: GameDao

    abstract val wordsDao: WordsDao

    abstract val pangramsDao: PangramsDao
}