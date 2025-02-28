package com.developer.lamarao.soletra.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier

fun Modifier.clickableNoRipple(onClick: () -> Unit) =
    this.clickable(
        interactionSource = MutableInteractionSource(),
        indication = null,
        onClick = onClick
    )
