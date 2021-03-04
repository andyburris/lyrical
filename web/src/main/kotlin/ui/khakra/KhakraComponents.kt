package ui.khakra

import com.github.mpetuska.khakra.KhakraDSL
import com.github.mpetuska.khakra.kt.Builder
import com.github.mpetuska.khakra.layout.Heading
import com.github.mpetuska.khakra.layout.HeadingProps
import com.github.mpetuska.khakra.layout.Text
import com.github.mpetuska.khakra.layout.TextProps
import react.RBuilder
import react.RElementBuilder
import react.ReactElement

@KhakraDSL
public inline fun RBuilder.Heading1(
    noinline props: Builder<HeadingProps> = {},
    crossinline block: Builder<RElementBuilder<HeadingProps>> = {},
): ReactElement = Heading({ size = "4xl"; `as` = "h1"; textStyle="h1"; props() }, block)

@KhakraDSL
public inline fun RBuilder.SectionHeader(
    noinline props: Builder<TextProps> = {},
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({fontSize = "xl"; textStyle="sectionHeader"; props();}, block)

@KhakraDSL
public inline fun RBuilder.Subtitle1(
    noinline props: Builder<TextProps> = {},
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({fontSize = "xl"; textStyle="subtitle1"; props();}, block)

@KhakraDSL
public inline fun RBuilder.Subtitle2(
    noinline props: Builder<TextProps> = {},
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({fontSize = "lg"; textStyle="subtitle2"; props();}, block)

@KhakraDSL
public inline fun RBuilder.Body1(
    noinline props: Builder<TextProps> = {},
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({fontSize = "xl"; props();}, block)

@KhakraDSL
public inline fun RBuilder.Body2(
    noinline props: Builder<TextProps> = {},
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({fontSize = "lg"; textStyle="body2"; props();}, block)