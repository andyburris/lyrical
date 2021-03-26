package ui.landing

import ContentColumn
import Platform
import ScreenPadding
import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.button.ButtonIcon
import com.github.mpetuska.khakra.kt.KhakraComponent
import com.github.mpetuska.khakra.kt.set
import com.github.mpetuska.khakra.layout.Box
import com.github.mpetuska.khakra.layout.HStack
import com.github.mpetuska.khakra.layout.VStack
import flexColumn
import flexRow
import formattedName
import getPlatform
import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.html.js.onClickFunction
import onHorizontalLayout
import react.RBuilder
import react.RProps
import react.functionalComponent
import react.child
import react.dom.h1
import react.dom.p
import recordOf
import size
import styled.*
import ui.common.Icon
import ui.khakra.*
import ui.theme
import kotlin.random.Random

fun RBuilder.LandingScreen(onAuthenticate: (AuthAction.Authenticate) -> Unit) = child(landingScreen) {
    attrs {
        this.onAuthenticate = onAuthenticate
    }
}
external interface LandingProps : RProps {
    var onAuthenticate: (AuthAction.Authenticate) -> Unit
}
val landingScreen = functionalComponent<LandingProps> { props ->
    VStack({ spacing = 0; alignItems = "stretch" }) {
        Box({ backgroundColor = colorTheme() +  "primary" }) {
            VStack({ spacing = arrayOf(32, 48, 64); maxW = "1280px"; alignItems = "start"; p = arrayOf("32", "48", "64"); minH = "calc(100vh - 128px)"; justifyContent = "center"; margin = "auto" }) {
                LandingContent(props.onAuthenticate)
            }
        }
        Box({ backgroundColor = colorTheme() +  "primary" }) {
            VStack({ spacing = arrayOf(32, 48, 64); maxW = "1280px"; alignItems = "stretch"; px = arrayOf("32", "48", "64"); py = 0; justifyContent = "center"; margin = "auto" }) {
                PreviewContent()
            }
        }
        Box() {
            VStack({ spacing = arrayOf(32, 48, 64); maxW = "1280px"; alignItems = "start"; p = arrayOf("32", "48", "64"); minH = "100vh"; justifyContent = "center"; margin = "auto" }) {
                AppContent()
            }
        }
    }
}

private fun RBuilder.LandingContent(onAuthenticate: (AuthAction.Authenticate) -> Unit) {
    flexColumn(gap = 64.px, justifyContent = JustifyContent.center) {
        flexColumn(gap = 16.px) {
            flexRow(alignItems = Align.center, gap = 32.px) {
                styledImg(src = "/assets/LyricalIcon.svg") {
                    css {
                        width = 72.px
                        height = 72.px
                        border(4.px, BorderStyle.solid, theme.primaryDark, 8.px)
                    }
                }
                Heading1(textColor = "onPrimary") { +"Lyrical" }
            }
            Subtitle1(textColor = "onPrimarySecondary") { +"Get a random lyric from your playlist and guess what song it’s from. Simple." }
        }
        flexColumn(gap = 16.px) {
            Button({
                variant = "solid"
                backgroundColor = colorTheme() + "background"
                color = colorTheme() + "onBackground"
                size = "fabStatic"
                onClick = {
                    onAuthenticate.invoke(AuthAction.Authenticate(Random.nextInt().toString()))
                }
            }) {
                +"LOG IN WITH SPOTIFY"
            }
            Body2(textColor = "onPrimaryTernary") { +"Lyrical only has access to read your username and playlists" }
        }
    }
}

private fun RBuilder.PreviewContent() {
    flexColumn(alignItems = Align.center) {
        BrowserPreview()
        flexColumn(justifyContent = JustifyContent.flexEnd, alignItems = Align.center) {
            css {
                width = 100.pct
            }
            styledDiv {
                css {
                    position = Position.absolute
                    width = 100.pct
                    height = 96.px
                    backgroundColor = Color.black.withAlpha(0.12).blend(theme.backgroundDark)
                    this.put("clip-path", "polygon(0 0, 0% 100%, 100% 100%);")
                }
            }
            styledDiv {
                css {
                    position = Position.absolute
                    width = 100.pct
                    height = 96.px
                    backgroundColor = theme.background
                    this.put("clip-path", "polygon(100% 0, 0% 100%, 100% 100%);")
                }
            }
        }
    }
}

private fun RBuilder.BrowserPreview() {
    flexColumn {
        css { width = 100.pct }
        flexRow(gap = 16.px, alignItems = Align.center) {
            css {
                width = 100.pct
                backgroundColor = theme.onPrimarySecondary
                borderTopLeftRadius = 16.px
                borderTopRightRadius = 16.px
                padding(16.px)
                boxSizing = BoxSizing.borderBox
            }
            flexRow(gap = 8.px) {
                css { opacity = 0.6 }
                Circle(16.px, Color.red)
                Circle(16.px, Color.gold)
                Circle(16.px, Color.limeGreen)
            }
            styledDiv { //Omnibar
                css {
                    height = 24.px
                    backgroundColor = theme.onPrimaryTernary
                    borderRadius = 12.px
                    flexGrow = 1.0
                }
            }
            Circle(16.px, Color.gray)
        }
        styledImg(src = "assets/LyricalDemo.png") {
            css { width = 100.pct }
        }
    }
}

private fun RBuilder.Circle(size: LinearDimension, color: Color) {
    styledDiv {
        css {
            size(size)
            borderRadius = size / 2
            backgroundColor = color
        }
    }
}

private fun RBuilder.AppContent() {
    flexColumn(gap = 32.px) {
        Heading1 { +"Download the app" }
        flexRow(wrap = FlexWrap.wrap, gap = 16.px) {
            val currentPlatform = getPlatform()
            val (current, other) = Platform.values().filter { it != Platform.Other }.partition { it == currentPlatform }
            current.forEach { AppChip(it, true) }
            other.forEach { AppChip(it, false) }
        }
    }
}

val Platform.icon get() = when(this) {
    Platform.Android -> Icon.Platform.Android
    Platform.Windows -> Icon.Platform.Windows
    Platform.IOS, Platform.MacOS -> Icon.Platform.Apple
    else -> Icon.Platform.Desktop
}
private fun RBuilder.AppChip(platform: Platform, currentPlatform: Boolean) {
    Button({
        variant = "solid"
        size = "fabStatic"
        backgroundColor = colorTheme() + if (currentPlatform) "primary" else "overlay"
        color = colorTheme() + if (currentPlatform) "onPrimary" else "onBackground"
    }) {
        HStack({ spacing = 16 }) {
            Icon(platform.icon)
            Subtitle1({
                textColor = colorTheme() + if (currentPlatform) "onPrimary" else "onBackground"
                this["_hover"] = recordOf(
                    "textColor" to colorTheme() + "onPrimary"
                )
            }) { +"Download for ${platform.formattedName}" }
        }
    }
}