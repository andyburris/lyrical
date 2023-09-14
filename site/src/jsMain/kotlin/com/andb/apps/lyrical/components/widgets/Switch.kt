package com.andb.apps.lyrical.components.widgets

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.theme.InsetShadowStyle
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.OutsetShadowStyle
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.*
import com.varabyte.kobweb.silk.components.style.common.DisabledStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.CSSLengthOrPercentageValue
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Label

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    ) {
    Label(
        attrs = modifier
            .toAttrs()
    ) {
        val trackSize = LyricalTheme.Size.Switch.Track
        Input(
            type = InputType.Checkbox,
            value = checked,
            onValueChanged = { onCheckedChange(!checked) },
            variant = SwitchInputVariant,
            enabled = enabled,
        )
        Box(
            SwitchTrackStyle.toModifier()
                .then(InsetShadowStyle.toModifier())
                .width(trackSize.width)
                .height(trackSize.height)
                .backgroundColor(if (checked) LyricalTheme.Colors.accentPalette.background else LyricalTheme.palette.backgroundDark)
                .thenIf(!enabled) { DisabledStyle.toModifier() }
        ) {
            Box(
                SwitchThumbStyle.toModifier()
                    .then(OutsetShadowStyle.toModifier())
                    .margin(4.px)
                    .size(LyricalTheme.Size.Switch.Thumb)
                    .setVariable(
                        SwitchVars.ThumbOffset,
                        if (checked) (trackSize.width - trackSize.height).unsafeCast<CSSLengthOrPercentageValue>() else 0.percent
                    )
            )
        }
    }
}