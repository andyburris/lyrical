package platform

import compose.multiplatform.ui.Modifier
import androidx.compose.runtime.Composable
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.shape.Shape

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    shape: Shape = CircleShape,
    content: @Composable () -> Unit,
) = Button(onClick, modifier, isEnabled, shape, CurrentPalette.invert(), content)

@Composable
expect fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    shape: Shape = CircleShape,
    buttonColors: Palette/* = CurrentPalette.invert()*/,
    content: @Composable () -> Unit,
)

/*
data class ButtonColors(
    val backgroundColor: Color,
    val disabledBackgroundColor: Color = backgroundColor.copy(alpha = 0.25f),
    val contentColor: Color,
    val disabledContentColor: Color = contentColor.copy(alpha = 0.25f)
)*/
