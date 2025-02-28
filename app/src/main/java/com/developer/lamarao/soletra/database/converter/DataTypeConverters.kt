package com.developer.lamarao.soletra.database.converter

import androidx.room.TypeConverter
import com.developer.lamarao.soletra.database.model.Word
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataTypeConverters {

    @TypeConverter
    fun fromWord(data: List<Word>): String {
        return Json.encodeToString(data)
    }

    @TypeConverter
    fun toWord(json: String): List<Word> {
        return Json.decodeFromString<List<Word>>(json)
    }

    @TypeConverter
    fun fromListString(data: List<String>): String {
        return Json.encodeToString(data)
    }

    @TypeConverter
    fun toStringList(json: String): List<String> {
        return Json.decodeFromString<List<String>>(json)
    }
}