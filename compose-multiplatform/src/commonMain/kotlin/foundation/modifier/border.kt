package compose.multiplatform.foundation.modifier

import compose.multiplatform.ui.unit.Dp
import compose.multiplatform.ui.Color
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.RectangleShape
import compose.multiplatform.ui.shape.Shape

expect fun Modifier.border(width: Dp, color: Color, shape: Shape = RectangleShape): Modifier
