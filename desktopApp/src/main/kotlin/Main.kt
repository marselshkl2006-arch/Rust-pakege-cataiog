import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.App
import cratescatalog.res.Res
import cratescatalog.res.app_title
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    val windowTitle = stringResource(Res.string.app_title)
    Window(
        onCloseRequest = ::exitApplication,
        title = windowTitle,
        state = rememberWindowState(size = DpSize(960.dp, 720.dp)),
    ) {
        App()
    }
}
