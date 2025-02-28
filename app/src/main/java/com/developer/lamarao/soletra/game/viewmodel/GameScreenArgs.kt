package com.developer.lamarao.soletra.game.viewmodel

import com.developer.lamarao.soletra.database.model.Game
import kotlinx.serialization.Serializable

@Serializable
data class GameScreenArgs(
    val game: Game?
)