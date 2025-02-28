package com.developer.lamarao.soletra.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developer.lamarao.soletra.database.model.Words

@Dao
interface WordsDao {

    @Query("SELECT * FROM Words")
    suspend fun getAllWords(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Words)

}