package compose.multiplatform.foundation.modifier

import compose.multiplatform.ui.Modifier

expect fun Modifier.clickable(onClick: () -> Unit): Modifier
