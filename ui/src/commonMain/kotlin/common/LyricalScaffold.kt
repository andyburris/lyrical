package common

import androidx.compose.runtime.Composable
import compose.multiplatform.foundation.layout.Box
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.unit.dp

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