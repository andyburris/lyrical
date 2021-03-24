package ui.khakra

import com.github.mpetuska.khakra.KhakraDSL
import com.github.mpetuska.khakra.colorMode.useColorMode
import com.github.mpetuska.khakra.kt.Builder
import com.github.mpetuska.khakra.layout.*
import react.RBuilder
import react.RElementBuilder
import react.ReactElement

@KhakraDSL
public inline fun RBuilder.Heading1(
    noinline props: Builder<HeadingProps> = {},
    textColor: String = "onBackground",
    crossinline block: Builder<RElementBuilder<HeadingProps>> = {},
): ReactElement = Heading({
    `as` = "h1"
    textStyle = "h1"
    this.textColor = colorTheme() + textColor
    props()
}, block)

@KhakraDSL
public inline fun RBuilder.Heading2(
    noinline props: Builder<HeadingProps> = {},
    textColor: String = "onBackground",
    crossinline block: Builder<RElementBuilder<HeadingProps>> = {},
): ReactElement = Heading({
    `as` = "h2"
    textStyle = "h2"
    this.textColor = colorTheme() + textColor
    props()
}, block)

@KhakraDSL
public inline fun RBuilder.SectionHeader(
    noinline props: Builder<TextProps> = {},
    textColor: String = "onBackgroundSecondary",
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({
    textStyle = "sectionHeader"
    this.textColor = colorTheme() + textColor
    props()
}, block)

@KhakraDSL
public inline fun RBuilder.Subtitle1(
    noinline props: Builder<TextProps> = {},
    textColor: String = "onBackground",
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({
    textStyle = "subtitle1"
    this.textColor = colorTheme() + textColor
    props()
}, block)

@KhakraDSL
public inline fun RBuilder.Subtitle2(
    noinline props: Builder<TextProps> = {},
    textColor: String = "onBackground",
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({
    textStyle = "subtitle2"
    this.textColor = colorTheme() + textColor
    props()
}, block)

@KhakraDSL
public inline fun RBuilder.Body1(
    noinline props: Builder<TextProps> = {},
    textColor: String = "onBackgroundSecondary",
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({
    textStyle = "body1"
    this.textColor = colorTheme() + textColor
    props()
}, block)

@KhakraDSL
public inline fun RBuilder.Body2(
    noinline props: Builder<TextProps> = {},
    textColor: String = "onBackgroundSecondary",
    crossinline block: Builder<RElementBuilder<TextProps>> = {},
): ReactElement = Text({
    textStyle = "body2"
    this.textColor = colorTheme() + textColor
    props()
}, block)

