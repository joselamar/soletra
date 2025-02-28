package com.developer.lamarao.soletra.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.developer.lamarao.soletra.database.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM Game")
    suspend fun getGame(): Game?

    @Insert
    suspend fun insertGame(game: Game)

    @Update
    suspend fun updateGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)
}