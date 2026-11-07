package ui.dependencies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cratescatalog.res.Res
import cratescatalog.res.back
import cratescatalog.res.dependencies
import cratescatalog.res.no_dependencies
import org.jetbrains.compose.resources.stringResource
import ui.common.ErrorMessage
import ui.common.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DependenciesScreen(
    crateName: String,
    state: DependenciesState,
    onBack: () -> Unit,
    onDependencyClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("${stringResource(Res.string.dependencies)}: $crateName") },
                actions = { TextButton(onClick = onBack) { Text(stringResource(Res.string.back)) } },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.error != null && state.dependencies.isEmpty() -> ErrorMessage(
                    message = state.error.orEmpty(),
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center),
                )

                state.isLoading -> LoadingIndicator(Modifier.align(Alignment.Center))

                state.dependencies.isEmpty() -> Text(
                    stringResource(Res.string.no_dependencies),
                    modifier = Modifier.align(Alignment.Center).padding(24.dp),
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(items = state.dependencies, key = { it }) { dep ->
                        Card(Modifier.fillMaxWidth().clickable { onDependencyClick(dep) }) {
                            Column(Modifier.padding(16.dp)) {
                                Text(dep, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
