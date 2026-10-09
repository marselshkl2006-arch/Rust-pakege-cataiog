package ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import cratescatalog.res.Res
import cratescatalog.res.back
import cratescatalog.res.dependencies
import cratescatalog.res.description_missing
import cratescatalog.res.documentation
import cratescatalog.res.downloads
import cratescatalog.res.loading
import cratescatalog.res.owner
import cratescatalog.res.repository
import cratescatalog.res.version
import cratescatalog.res.versions
import model.CrateDetail
import org.jetbrains.compose.resources.stringResource
import ui.common.ErrorMessage
import ui.common.LoadingIndicator
import ui.common.formatNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: DetailState,
    onBack: () -> Unit,
    onDependencies: () -> Unit,
    onOwner: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val detail = state.crate
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(detail?.crate?.name ?: stringResource(Res.string.loading)) })
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                detail != null -> DetailContent(detail, onBack, onDependencies, onOwner)
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
private fun DetailContent(
    detail: CrateDetail,
    onBack: () -> Unit,
    onDependencies: () -> Unit,
    onOwner: (String) -> Unit,
) {
    val crate = detail.crate
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(crate.name, style = MaterialTheme.typography.headlineMedium)
        Text(
            "${stringResource(Res.string.version)}: ${crate.version}",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            crate.description ?: stringResource(Res.string.description_missing),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text("${stringResource(Res.string.downloads)}: ${formatNumber(crate.downloads)}")
        crate.repository?.let { LinkRow(stringResource(Res.string.repository), it) }
        crate.documentation?.let { LinkRow(stringResource(Res.string.documentation), it) }

        if (detail.versions.isNotEmpty()) {
            Text(stringResource(Res.string.versions), style = MaterialTheme.typography.titleSmall)
            detail.versions.take(5).forEach { v ->
                Text(
                    "${v.num} - ${formatNumber(v.downloads)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Button(onClick = onDependencies) { Text(stringResource(Res.string.dependencies)) }
        detail.owners.forEach { owner ->
            OutlinedButton(onClick = { onOwner(owner.login) }) {
                Text("${stringResource(Res.string.owner)}: ${owner.login}")
            }
        }
        TextButton(onClick = onBack) { Text(stringResource(Res.string.back)) }
    }
}

@Composable
private fun LinkRow(label: String, url: String) {
    val uriHandler = LocalUriHandler.current
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("$label:")
        Text(
            text = url,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.fillMaxWidth().clickable { runCatching { uriHandler.openUri(url) } },
        )
    }
}
