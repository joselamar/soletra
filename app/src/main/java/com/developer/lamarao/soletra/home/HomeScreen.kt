package com.developer.lamarao.soletra.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.developer.lamarao.soletra.destinations.GameScreenDestination
import com.developer.lamarao.soletra.home.viewmodel.HomeViewModel
import com.developer.lamarao.soletra.home.viewmodel.HomeViewModel.HomeScreenUiState
import com.developer.lamarao.soletra.home.viewmodel.HomeViewModel.UiEvent.NavigateToGame
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@RootNavGraph(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by homeViewModel.viewState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current.lifecycle
    val lifecycleState by lifecycleOwner.currentStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(homeViewModel) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            homeViewModel.uiEvent.collect { event ->
                when (event) {
                    is NavigateToGame -> navigator.navigate(GameScreenDestination(game = event.game))
                }
            }
        }
    }

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                homeViewModel.onResume()
            }

            else -> Unit
        }
    }

    HomeScreenContent(
        state = state,
        onContinueGameClicked = {
            homeViewModel.onContinueGameClicked()
        },
        onNewGameClicked = {
            homeViewModel.onNewGameClicked()
        }
    )
}

@Composable
fun HomeScreenContent(
    state: HomeScreenUiState,
    onContinueGameClicked: () -> Unit,
    onNewGameClicked: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(0.3f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Soletra",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displayLarge
                )
            }
            Column(
                modifier = Modifier.weight(0.7f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = state.currentGame != null) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 48.dp),
                        onClick = {
                            onContinueGameClicked()
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(12.dp),
                            text = "Continuar",
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    onClick = {
                        onNewGameClicked()
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = "Novo Jogo",
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreenContent(
        state = HomeScreenUiState(null),
        onContinueGameClicked = {},
        onNewGameClicked = {}
    )
}