package com.developer.lamarao.soletra.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developer.lamarao.soletra.database.model.Pangram
import com.developer.lamarao.soletra.database.model.Words

@Dao
interface PangramsDao {

    @Query("SELECT * FROM Pangram")
    suspend fun getAllPangrams(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPangram(word: Pangram)
}