package app

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import data.Cache
import data.CratesApi
import data.CratesRepository
import data.createCratesClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import locale.LocalizedContent
import navigation.Navigator
import navigation.Screen
import state.AppState
import theme.AppTheme
import ui.dependencies.DependenciesScreen
import ui.dependencies.DependenciesState
import ui.detail.DetailScreen
import ui.detail.DetailState
import ui.list.ListScreen
import ui.list.ListState
import ui.owner.OwnerScreen
import ui.owner.OwnerState

private const val SLIDE_MS = 300

@Composable
fun App() {
    val systemDark = isSystemInDarkTheme()
    val appState = remember { AppState(isDarkTheme = systemDark) }
    val navigator = remember { Navigator() }
    val listState = remember { ListState() }
    val listScrollState = rememberLazyListState()

    val api = remember { CratesApi(createCratesClient(), Cache()) }
    DisposableEffect(api) { onDispose { api.close() } }
    val repository = remember(api) { CratesRepository(api = api, useMocks = { appState.useMocks }) }

    LocalizedContent(language = appState.language) {
        AppTheme(darkTheme = appState.isDarkTheme) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                NavDisplay(
                    backStack = navigator.backStack,
                    onBack = { navigator.goBack() },
                    // Forward: new screen slides in from the right.
                    transitionSpec = {
                        slideInHorizontally(tween(SLIDE_MS)) { it } togetherWith
                            slideOutHorizontally(tween(SLIDE_MS)) { -it / 3 }
                    },
                    // Back: previous screen slides in from the left, current one leaves to the right.
                    popTransitionSpec = {
                        slideInHorizontally(tween(SLIDE_MS)) { -it } togetherWith
                            slideOutHorizontally(tween(SLIDE_MS)) { it }
                    },
                    predictivePopTransitionSpec = {
                        slideInHorizontally(tween(SLIDE_MS)) { -it } togetherWith
                            slideOutHorizontally(tween(SLIDE_MS)) { it }
                    },
                    entryProvider = entryProvider {
                        entry<Screen.List> {
                            val scope = rememberCoroutineScope()
                            LaunchedEffect(listState.query, appState.useMocks) {
                                if (listState.needsReload(appState.useMocks)) {
                                    if (listState.query.isNotBlank()) delay(300) // debounce typing
                                    listState.loadFirstPage(repository, appState.useMocks)
                                }
                            }
                            ListScreen(
                                state = listState,
                                language = appState.language,
                                useMocks = appState.useMocks,
                                onQueryChange = { listState.query = it },
                                onItemClick = { navigator.navigateTo(Screen.Detail(it.name)) },
                                onLoadMore = { scope.launch { listState.loadNextPage(repository) } },
                                onRetry = { scope.launch { listState.loadFirstPage(repository, appState.useMocks) } },
                                onToggleTheme = { appState.toggleTheme() },
                                onToggleLanguage = {
                                    appState.changeLanguage(if (appState.language == "ru") "en" else "ru")
                                },
                                onToggleDataSource = { appState.toggleDataSource() },
                                lazyListState = listScrollState,
                            )
                        }
                        entry<Screen.Detail> { route ->
                            val state = remember(route.crateName) { DetailState() }
                            var retry by remember { mutableIntStateOf(0) }
                            LaunchedEffect(route.crateName, appState.useMocks, retry) {
                                state.load(repository, route.crateName)
                            }
                            DetailScreen(
                                state = state,
                                onBack = { navigator.goBack() },
                                onDependencies = { navigator.navigateTo(Screen.Dependencies(route.crateName)) },
                                onOwner = { login -> navigator.navigateTo(Screen.Owner(login)) },
                                onRetry = { retry++ },
                            )
                        }
                        entry<Screen.Dependencies> { route ->
                            val state = remember(route.crateName) { DependenciesState() }
                            var retry by remember { mutableIntStateOf(0) }
                            LaunchedEffect(route.crateName, appState.useMocks, retry) {
                                state.load(repository, route.crateName)
                            }
                            DependenciesScreen(
                                crateName = route.crateName,
                                state = state,
                                onBack = { navigator.goBack() },
                                onDependencyClick = { dep -> navigator.navigateTo(Screen.Detail(dep)) },
                                onRetry = { retry++ },
                            )
                        }
                        entry<Screen.Owner> { route ->
                            val state = remember(route.login) { OwnerState() }
                            var retry by remember { mutableIntStateOf(0) }
                            LaunchedEffect(route.login, appState.useMocks, retry) {
                                state.load(repository, route.login)
                            }
                            OwnerScreen(
                                login = route.login,
                                state = state,
                                onBack = { navigator.goBack() },
                                onRetry = { retry++ },
                            )
                        }
                    },
                )
            }
        }
    }
}
