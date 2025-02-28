package com.developer.lamarao.soletra.game.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.RoundedPolygon
import com.developer.lamarao.soletra.core.ui.clickableNoRipple

@Composable
fun Hexagon(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 6,
                    radius = size.minDimension / 2,
                    centerX = size.width / 2,
                    centerY = size.height / 2
                )
                val roundedPolygonPath = roundedPolygon.cubics.toPath()
                onDrawBehind {
                    drawPath(roundedPolygonPath, color = color)
                }
            }
            .size(100.dp)
            .clickableNoRipple { onClick() },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}



fun List<Cubic>.toPath(path: Path = Path(), scale: Float = 1f): Path {
    path.rewind()
    firstOrNull()?.let { first ->
        path.moveTo(first.anchor0X * scale, first.anchor0Y * scale)
    }
    for (bezier in this) {
        path.cubicTo(
            bezier.control0X * scale, bezier.control0Y * scale,
            bezier.control1X * scale, bezier.control1Y * scale,
            bezier.anchor1X * scale, bezier.anchor1Y * scale
        )
    }
    path.close()
    return path
}