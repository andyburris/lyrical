package ui.khakra

import com.github.mpetuska.khakra.KhakraDSL
import com.github.mpetuska.khakra.kt.Builder
import com.github.mpetuska.khakra.layout.*
import react.RBuilder
import react.RElementBuilder
import react.ReactElement

@KhakraDSL
public inline fun RBuilder.Heading1(
    noinline props: Builder<HeadingProps> = {},
    crossinline block: Builder<RElementBuilder<HeadingProps>> = {},
): ReactElement = Heading({ size = "3xl"; `as` = "h1"; textStyle="h1"; props() }, block)

@KhakraDSL
public inline fun RBuilder.SectionHeader(
    noinline props: Builder<TextProps> = {},
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({fontSize = "2xl"; textStyle="sectionHeader"; props();}, block)

@KhakraDSL
public inline fun RBuilder.Subtitle1(
    noinline props: Builder<TextProps> = {},
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({fontSize = "2xl"; textStyle="subtitle1"; props();}, block)

@KhakraDSL
public inline fun RBuilder.Subtitle2(
    noinline props: Builder<TextProps> = {},
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({fontSize = "lg"; textStyle="subtitle2"; props();}, block)

@KhakraDSL
public inline fun RBuilder.Body1(
    noinline props: Builder<TextProps> = {},
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({fontSize = "2xl"; props();}, block)

@KhakraDSL
public inline fun RBuilder.Body2(
    noinline props: Builder<TextProps> = {},
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({fontSize = "lg"; textStyle="body2"; props();}, block)

