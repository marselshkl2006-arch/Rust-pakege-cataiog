package ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cratescatalog.res.Res
import cratescatalog.res.app_title
import cratescatalog.res.data_source_mock
import cratescatalog.res.data_source_network
import cratescatalog.res.description_missing
import cratescatalog.res.downloads
import cratescatalog.res.language_toggle
import cratescatalog.res.search_hint
import cratescatalog.res.theme_toggle
import model.Crate
import org.jetbrains.compose.resources.stringResource
import ui.common.EmptyState
import ui.common.ErrorMessage
import ui.common.LoadingIndicator
import ui.common.formatNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    state: ListState,
    language: String,
    useMocks: Boolean,
    onQueryChange: (String) -> Unit,
    onItemClick: (Crate) -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleDataSource: () -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val visible = state.filtered

    val nearEnd by remember(lazyListState) {
        derivedStateOf {
            val info = lazyListState.layoutInfo
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            info.totalItemsCount > 0 && last >= info.totalItemsCount - 3
        }
    }
    LaunchedEffect(nearEnd, state.isLoading, state.endReached, state.error, visible.size) {
        if (nearEnd && !state.isLoading && !state.endReached && state.error == null) onLoadMore()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_title)) },
                actions = {
                    TextButton(onClick = onToggleDataSource) {
                        Text(
                            stringResource(
                                if (useMocks) Res.string.data_source_mock else Res.string.data_source_network,
                            ),
                        )
                    }
                    TextButton(onClick = onToggleTheme) { Text(stringResource(Res.string.theme_toggle)) }
                    TextButton(onClick = onToggleLanguage) {
                        Text(if (language == "ru") "EN" else "RU")
                    }
                },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                singleLine = true,
                label = { Text(stringResource(Res.string.search_hint)) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            )
            Box(Modifier.fillMaxSize()) {
                when {
                    state.error != null && visible.isEmpty() ->
                        ErrorMessage(
                            message = state.error.orEmpty(),
                            onRetry = onRetry,
                            modifier = Modifier.align(Alignment.Center),
                        )

                    state.isLoading && visible.isEmpty() ->
                        LoadingIndicator(Modifier.align(Alignment.Center))

                    visible.isEmpty() -> EmptyState(Modifier.align(Alignment.Center))

                    else -> LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(items = visible, key = { it.name }) { crate ->
                            CrateItem(crate = crate, onClick = { onItemClick(crate) })
                        }
                        if (state.isLoading) {
                            item(key = "loading-more") {
                                LoadingIndicator(Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CrateItem(crate: Crate, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "${crate.name}  ${crate.version}",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = crate.description ?: stringResource(Res.string.description_missing),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${stringResource(Res.string.downloads)}: ${formatNumber(crate.downloads)}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
