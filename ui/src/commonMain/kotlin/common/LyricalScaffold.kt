package common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.tooling.LocalInspectionTables
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp

@OptIn(ExperimentalComposeWebWidgetsApi::class)
@Composable
fun LyricalScaffold(
    modifier: Modifier = Modifier,
    appBar: @Composable () -> Unit,
    title: @Composable () -> Unit,
    actionButton: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val width = 720.dp //TODO: add BoxWithConstraints
    //val isMobileLayout = width > 720.dp //TODO: [Compose] add compareTo to Dp class
    val isMobileLayout = false
    Column(modifier) {
        appBar()
        Row {
            title()
            if (!isMobileLayout) {
                actionButton()
            }
        }
        Box {
            content()
            if (isMobileLayout) {
                Box(Modifier.padding(32.dp)) {
                    actionButton()
                }
            }
        }
    }
}