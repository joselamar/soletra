package com.developer.lamarao.soletra.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "Words")
data class Words(
    @PrimaryKey
    val word: String,
)

@Entity(tableName = "Pangram")
data class Pangram(
    @PrimaryKey
    val pangram: String,
)

@Serializable
data class Word(
    val text: String,
    val isDiscovered: Boolean? = false
)
