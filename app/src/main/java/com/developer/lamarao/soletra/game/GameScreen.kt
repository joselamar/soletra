package com.developer.lamarao.soletra.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.developer.lamarao.soletra.R
import com.developer.lamarao.soletra.core.transition.Transitions
import com.developer.lamarao.soletra.core.ui.clickableNoRipple
import com.developer.lamarao.soletra.database.model.Game
import com.developer.lamarao.soletra.database.model.Word
import com.developer.lamarao.soletra.destinations.InstructionsScreenDestination
import com.developer.lamarao.soletra.game.ui.Hexagon
import com.developer.lamarao.soletra.game.viewmodel.GameScreenArgs
import com.developer.lamarao.soletra.game.viewmodel.GameViewModel
import com.developer.lamarao.soletra.game.viewmodel.GameViewModel.UiEvent.NavigateBack
import com.developer.lamarao.soletra.game.viewmodel.GameViewModel.UiEvent.ShowSnackBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(
    navArgsDelegate = GameScreenArgs::class,
    style = Transitions::class
)
fun GameScreen(
    navigator: DestinationsNavigator,
    gameViewModel: GameViewModel = hiltViewModel()
) {
    val uiState by gameViewModel.viewState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current.lifecycle
    val lifecycleState by lifecycleOwner.currentStateFlow.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(gameViewModel) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            gameViewModel.uiEvent.collect { event ->
                when (event) {
                    is NavigateBack -> navigator.navigateUp()
                    is GameViewModel.UiEvent.ShowInstructions -> navigator.navigate(InstructionsScreenDestination)
                    is ShowSnackBar -> {
                        snackbarHostState.showSnackbar(context.getString(event.stringId))
                    }
                }
            }
        }
    }

    AnimatedVisibility(
        visible = uiState.currentGame == null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LoadingSection()
    }

    AnimatedVisibility(
        visible = uiState.currentGame != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        uiState.currentGame?.let { game ->
            GameScreenContent(
                snackbarHostState = snackbarHostState,
                game = game,
                onBackClicked = {
                    gameViewModel.onBackClicked()
                },
                onHintClicked = {
                    gameViewModel.onHintClicked()
                },
                onHelpClicked = {
                    gameViewModel.onHelpClicked()
                },
                onHexagonClicked = { letter ->
                    gameViewModel.onHexagonClicked(letter)
                },
                onDeleteClicked = {
                    gameViewModel.onDeleteClicked()
                },
                onShuffleClicked = {
                    gameViewModel.onShuffledClicked()
                },
                onConfirmClicked = {
                    gameViewModel.onConfirmClicked()
                }
            )
        }
    }

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {
                gameViewModel.saveGame()
            }

            else -> Unit
        }
    }
}

@Composable
fun LoadingSection() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "A criar jogo...",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.padding(16.dp))
            CircularProgressIndicator()
        }
    }
}

@Composable
fun GameScreenContent(
    snackbarHostState: SnackbarHostState,
    game: Game,
    onBackClicked: () -> Unit,
    onHintClicked: () -> Unit,
    onHelpClicked: () -> Unit,
    onHexagonClicked: (String) -> Unit,
    onDeleteClicked: () -> Unit,
    onShuffleClicked: () -> Unit,
    onConfirmClicked: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Row(Modifier.padding(16.dp)) {
                Icon(
                    modifier = Modifier.clickableNoRipple(onBackClicked),
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Voltar atrás"
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Pontuação: ${game.score}/${game.maxScore}")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
            Guess(game.currentGuess, onHintClicked, onHelpClicked)
            Board(game.middleLetter, game.remainingLetters, onHexagonClicked)
            Spacer(modifier = Modifier.height(16.dp))
            GuessHandler(onDeleteClicked, onShuffleClicked, onConfirmClicked)
            Spacer(modifier = Modifier.height(16.dp))
            FoundWordsSection(game.words)
        }
    }
}

@Composable
fun Guess(
    text: String,
    onHintClicked: () -> Unit,
    onHelpClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.align(Alignment.Center)) {
            Text(
                text = text.ifEmpty { "PALAVRA" },
                style = MaterialTheme.typography.headlineSmall,
                color = if (text.isEmpty()) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
            )
        }
        Box(
            Modifier.align(Alignment.TopEnd)
        ) {
            Column(
                modifier = Modifier.padding(end = 16.dp)
            ) {
                IconButton(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .size(40.dp),
                    onClick = onHelpClicked
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = R.drawable.ic_question_mark),
                        contentDescription = "Instruções",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                IconButton(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .size(40.dp),
                    onClick = onHintClicked
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = R.drawable.ic_lightbulb),
                        contentDescription = "Pista",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun Board(
    middleLetter: String,
    remainingLetters: List<String>,
    onHexagonClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Hexagon(
                modifier = Modifier.offset((14).dp, (2.dp)),
                onClick = { onHexagonClicked(remainingLetters.getOrNull(0).orEmpty()) }
            ) {
                Text(
                    text = remainingLetters.getOrNull(0).orEmpty(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            Hexagon(
                modifier = Modifier.offset((14).dp, ((-2).dp)),
                onClick = { onHexagonClicked(remainingLetters.getOrNull(1).orEmpty()) }
            ) {
                Text(
                    text = remainingLetters.getOrNull(1).orEmpty(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
        }
        Column {
            Hexagon(
                Modifier.offset(y = (4).dp),
                onClick = { onHexagonClicked(remainingLetters.getOrNull(2).orEmpty()) }
            ) {
                Text(
                    text = remainingLetters.getOrNull(2).orEmpty(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            Hexagon(
                color = MaterialTheme.colorScheme.primary,
                onClick = { onHexagonClicked(middleLetter) }
            ) {
                Text(
                    text = middleLetter,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Hexagon(
                Modifier.offset(y = (-4).dp),
                onClick = { onHexagonClicked(remainingLetters.getOrNull(3).orEmpty()) }
            ) {
                Text(
                    text = remainingLetters.getOrNull(3).orEmpty(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
        }
        Column {
            Hexagon(
                modifier = Modifier.offset((-14).dp, (2.dp)),
                onClick = { onHexagonClicked(remainingLetters.getOrNull(4).orEmpty()) }
            ) {
                Text(
                    text = remainingLetters.getOrNull(4).orEmpty(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            Hexagon(
                modifier = Modifier.offset((-14).dp, (-2).dp),
                onClick = { onHexagonClicked(remainingLetters.getOrNull(5).orEmpty()) }
            ) {
                Text(
                    text = remainingLetters.getOrNull(5).orEmpty(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
        }
    }
}

@Composable
fun GuessHandler(
    onDeleteClicked: () -> Unit,
    onShuffleClicked: () -> Unit,
    onConfirmClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = spacedBy(16.dp),
    ) {
        Button(modifier = Modifier.weight(0.5f), onClick = onDeleteClicked) {
            Text(text = "Apagar")
        }
        Button(onClick = onShuffleClicked) {
            Icon(painterResource(id = R.drawable.ic_shuffle), contentDescription = "Baralhar")
        }
        Button(modifier = Modifier.weight(0.5f), onClick = onConfirmClicked) {
            Text(text = "Confirmar")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FoundWordsSection(wordList: List<Word>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp),
        verticalArrangement = spacedBy(8.dp)
    ) {
        Text(text = "Palavras Encontradas:")
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = spacedBy(4.dp)
        ) {
            wordList.forEach { word ->
                Text(
                    modifier = Modifier
                        .padding(4.dp)
                        .border(1.dp, color = MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    text = if (word.isDiscovered == true) word.text else "${word.text.length} letras",
                    textAlign = TextAlign.Center,
                    color = if (word.isDiscovered == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreenContent(
        snackbarHostState = SnackbarHostState(),
        game = Game(
            gameId = 0,
            currentGuess = "GUESS",
            score = 0,
            maxScore = 50,
            words = listOf(Word("")),
            middleLetter = "A",
            remainingLetters = listOf("B", "C", "D", "E", "F", "G")
        ),
        onBackClicked = {},
        onHintClicked = {},
        onHelpClicked = {},
        onHexagonClicked = {},
        onDeleteClicked = {},
        onShuffleClicked = {},
        onConfirmClicked = {}
    )
}

