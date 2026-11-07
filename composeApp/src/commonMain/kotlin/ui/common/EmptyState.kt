package ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cratescatalog.res.Res
import cratescatalog.res.empty_list
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(Res.string.empty_list),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(24.dp),
    )
}
