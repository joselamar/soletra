package com.developer.lamarao.soletra.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "Game")
@Serializable
data class Game(
    @PrimaryKey(autoGenerate = true)
    val gameId: Int? = 0,
    val currentGuess: String,
    val score: Int,
    val maxScore: Int,
    val words: List<Word>,
    val middleLetter: String,
    val remainingLetters: List<String>
)

