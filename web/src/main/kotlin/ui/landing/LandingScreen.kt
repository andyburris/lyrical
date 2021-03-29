package ui.landing

import ContentColumn
import Platform
import ScreenPadding
import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.button.ButtonIcon
import com.github.mpetuska.khakra.colorMode.useColorModeValue
import com.github.mpetuska.khakra.image.Image
import com.github.mpetuska.khakra.kt.KhakraComponent
import com.github.mpetuska.khakra.kt.set
import com.github.mpetuska.khakra.layout.Box
import com.github.mpetuska.khakra.layout.Circle
import com.github.mpetuska.khakra.layout.HStack
import com.github.mpetuska.khakra.layout.VStack
import com.github.mpetuska.khakra.mediaQuery.useBreakpointValue
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
import ui.ChakraTheme
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
            HStack({ spacing = arrayOf(16, 24, 32) }) {
                Image({
                    src = "/assets/LyricalIcon.svg"
                    boxSize = arrayOf(48, 64, 72)
                    border = "solid 4px"
                    borderColor = ChakraTheme.primaryDark
                    borderRadius = "8px"
                })
                Heading1(textColor = ChakraTheme.onPrimary) { +"Lyrical" }
            }
            Subtitle1(textColor = ChakraTheme.onPrimarySecondary) { +"Get a random lyric from your playlist and guess what song it’s from. Simple." }
        }
        flexColumn(gap = 16.px) {
            css {
            }
            Button({
                variant = "solidBackground"
                size = "fabStatic"
                onClick = {
                    onAuthenticate.invoke(AuthAction.Authenticate(Random.nextInt().toString()))
                }
            }) {
                +"LOG IN WITH SPOTIFY"
            }
            Body2(textColor = ChakraTheme.onPrimaryTernary) { +"Lyrical only has access to read your username and playlists" }
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
            Box({
                position = "absolute"
                width = "100%"
                height = arrayOf(32, 64, 96)
                backgroundColor = ChakraTheme.backgroundDark
                clipPath = "polygon(0 0, 0% 100%, 100% 100%);"
            })
            Box({
                position = "absolute"
                width = "100%"
                height = arrayOf(32, 64, 96)
                backgroundColor = ChakraTheme.backgroundDark
                clipPath = "polygon(100% 0, 0% 100%, 100% 100%);"
                mb = "2px"
            })
            Box({
                position = "absolute"
                width = "100%"
                height = arrayOf(32, 64, 96)
                backgroundColor = ChakraTheme.background
                clipPath = "polygon(100% 0, 0% 100%, 100% 100%);"
            })
        }
    }
}

private fun RBuilder.BrowserPreview() {
    val isMobile: Boolean = useBreakpointValue(arrayOf(true, false)) == true
    val isDarkMode: Boolean = useColorModeValue(light = false, dark = true)
    flexColumn {
        css { width = 100.pct }
        HStack({
            spacing = 16
            width = "100%"
            backgroundColor = ChakraTheme.backgroundDark
            borderTopLeftRadius = 16
            borderTopRightRadius = 16
            p = 16
            boxSizing = "border-box"
        }) {
            if (!isMobile) {
                flexRow(gap = 8.px) {
                    css { opacity = 0.6 }
                    Circle({ boxSize = 16; bg = Color.red.value })
                    Circle({ boxSize = 16; bg = Color.gold.value })
                    Circle({ boxSize = 16; bg = Color.limeGreen.value })
                }
            }
            Box({
                height = 24
                backgroundColor = ChakraTheme.overlay
                borderRadius = "full"
                flexGrow = 1
            })
            Circle({ boxSize = 16; bg = ChakraTheme.onBackgroundSecondary })
        }
        val image = "assets/preview/Preview" + (if (isMobile) "Mobile" else "Desktop") + (if (isDarkMode) "Dark" else "Light") + ".png"
        styledImg(src = image) {
            css { width = 100.pct }
        }
    }
}

private fun RBuilder.Circle(size: LinearDimension, color: String) {
    Circle({
        boxSize = size.value
        bg = color
    })
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
        variant = if (currentPlatform) "solid" else useColorModeValue("outline", "solidCard")
        size = "fabStatic"
    }) {
        HStack({ spacing = 16 }) {
            Icon(platform.icon)
            Subtitle1({
                textColor = "inherit"
            }) { +"Download for ${platform.formattedName}" }
        }
    }
}