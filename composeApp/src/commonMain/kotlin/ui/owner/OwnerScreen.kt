package ui.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import cratescatalog.res.Res
import cratescatalog.res.back
import cratescatalog.res.login
import cratescatalog.res.owner
import cratescatalog.res.user_name
import model.Owner
import org.jetbrains.compose.resources.stringResource
import ui.common.ErrorMessage
import ui.common.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerScreen(
    login: String,
    state: OwnerState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val owner = state.owner
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("${stringResource(Res.string.owner)}: $login") },
                actions = { TextButton(onClick = onBack) { Text(stringResource(Res.string.back)) } },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                owner != null -> OwnerContent(owner, Modifier.align(Alignment.TopCenter))
                state.error != null -> ErrorMessage(
                    message = state.error.orEmpty(),
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center),
                )

                else -> LoadingIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun OwnerContent(owner: Owner, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier.size(96.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = owner.login.take(1).uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            owner.avatar?.let {
                AsyncImage(model = it, contentDescription = owner.login, modifier = Modifier.fillMaxSize())
            }
        }
        Text("${stringResource(Res.string.login)}: ${owner.login}", style = MaterialTheme.typography.titleLarge)
        owner.name?.let { Text("${stringResource(Res.string.user_name)}: $it", style = MaterialTheme.typography.bodyLarge) }
    }
}
