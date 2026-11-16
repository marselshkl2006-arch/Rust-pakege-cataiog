package ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cratescatalog.res.Res
import cratescatalog.res.back
import cratescatalog.res.favorites_title
import cratescatalog.res.favorites_empty
import org.jetbrains.compose.resources.stringResource
import state.FavoritesState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    state: FavoritesState,
    onCrateClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.favorites_title)) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(stringResource(Res.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (state.favorites.isEmpty()) {
                Text(
                    text = stringResource(Res.string.favorites_empty),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.favorites) { name ->
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable { onCrateClick(name) }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(name, style = MaterialTheme.typography.titleMedium)
                                IconButton(onClick = { state.toggleFavorite(name) }) {
                                    Text("★", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
