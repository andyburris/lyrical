package org.jetbrains.compose.common.internal

import compose.multiplatform.ui.Modifier
import androidx.compose.ui.Modifier as JModifier

private class ModifierElement : JModifier.Element

class ActualModifier : Modifier.Element {
    var modifier: JModifier = ModifierElement()
}

fun Modifier.castOrCreate(): ActualModifier = (this as? ActualModifier) ?: ActualModifier()
