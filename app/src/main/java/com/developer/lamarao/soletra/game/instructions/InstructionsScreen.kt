package com.developer.lamarao.soletra.game.instructions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle

@Composable
@Destination(style = DestinationStyle.Dialog::class)
fun InstructionsScreen(
    navigator: DestinationsNavigator
) {
    Dialog(
        onDismissRequest = { navigator.navigateUp() }, properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = spacedBy(12.dp)
            ) {
                Text(
                    text = "Instruçōes",
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "O objetivo do jogo é encontrar o maior número de palavras possível através das 7 letras da área de jogo. Inserido nestas palavras têm que constar pelo menos uma vez a letra que se encontra no meio do favo. As palavras têm de ter no minimo 4 letras e no máximo 8.",
                    color = Color.Unspecified,
                    textAlign = TextAlign.Justify
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navigator.navigateUp() }
                ) {
                    Text(text = "Confirmar")
                }
            }
        }
    }
}

@Preview
@Composable
fun InstructionsScreenPreview() {
    InstructionsScreen(EmptyDestinationsNavigator)
}