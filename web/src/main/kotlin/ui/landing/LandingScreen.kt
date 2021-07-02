package ui.landing

import AuthAction
import Platform
import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.colorMode.useColorModeValue
import com.github.mpetuska.khakra.hooks.useDisclosure
import com.github.mpetuska.khakra.image.Image
import com.github.mpetuska.khakra.kt.Builder
import com.github.mpetuska.khakra.layout.*
import com.github.mpetuska.khakra.mediaQuery.useBreakpointValue
import com.github.mpetuska.khakra.transition.Collapse
import flexColumn
import flexRow
import formattedName
import getPlatform
import kotlinx.browser.window
import kotlinx.css.*
import react.*
import styled.css
import ui.ChakraTheme
import ui.common.Icon
import ui.khakra.*
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
            VStack({ spacing = arrayOf(64, 96, 128); maxW = "1280px"; alignItems = "start"; px = arrayOf(32, 48, 64); py = arrayOf(64, 96, 128);justifyContent = "center"; margin = "auto" }) {
                AppContent()
                AboutContent()
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
        AspectRatio({
            ratio = if (isMobile)  (388 / 778.0) else (1245 / 778.0)
            bg = "linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.8)), url('$image')"
            backgroundSize = "cover"
            width = "100%"
        }) {
            VStack({ height = "100%"; justifyContent = "flex-end"; pb = 128; spacing = 16 }) {
                SectionHeader { +"Don’t want to log in?" }
                VStack({ spacing = 16 }) {
                    val disclosure = useDisclosure()
                    val (selectedGenre, setSelectedGenre) = useState<String?> { null }
                    Button({ variant = "solid"; size = "fabStatic"; onClick = disclosure.onToggle }) { +"PLAY DEMO" }
                    Collapse({ `in` = disclosure.isOpen; animateOpacity = true; }) {
                        VStack({ spacing = 16 }) {
                            HStack({ spacing = 16 }) {
                                Button({ variant = "solidCard"; size = "fabStatic"; onClick = { setSelectedGenre("rock") }; opacity = if (selectedGenre == "rock" || selectedGenre == null) 1.0 else 0.2 }) { +"Rock" }
                                Button({ variant = "solidCard"; size = "fabStatic"; onClick = { setSelectedGenre("pop") }; opacity = if (selectedGenre == "pop" || selectedGenre == null) 1.0 else 0.2 }) { +"Pop" }
                                Button({ variant = "solidCard"; size = "fabStatic"; onClick = { setSelectedGenre("rap") }; opacity = if (selectedGenre == "rap" || selectedGenre == null) 1.0 else 0.2 }) { +"Rap" }
                            }
                            Collapse({ `in` = selectedGenre != null }) {
                                when (selectedGenre) {
                                    "rock" -> HStack({ spacing = 16 }) {
                                        Button({ variant = "solidCard"; size = "fabStatic"; onClick = { window.location.hash = "demo?genre=altrock" } }) { +"Alt Rock" }
                                    }
                                    "pop" -> HStack({ spacing = 16 }) {
                                        Button({ variant = "solidCard"; size = "fabStatic"; onClick = { window.location.hash = "demo?genre=pop" } }) { +"Modern Pop" }
                                    }
                                    "rap" -> HStack({ spacing = 16 }) {
                                        Button({ variant = "solidCard"; size = "fabStatic"; onClick = { window.location.hash = "demo?genre=rap" } }) { +"Modern Rap" }
                                    }
                                }
                            }
                        }
                    }
                }
            }
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

private fun RBuilder.AboutContent() {
    VStack({ spacing = 32; alignItems = "start" }) {
        Heading1 { +"About" }
        SimpleGrid({ columns = arrayOf(1, 2) ;spacing = arrayOf("16", "20", "24"); w = "100%" }) {
            AboutCard(Icon.Github, "Source Code") {
                +"Lyrical is an open-source application using Kotlin Multiplatform to share code between the web, desktop, and mobile apps.  See the source code on "
                Link(href = "https://github.com/andb3/lyrical") { +"Github" }
            }
            AboutCard(Icon.Link, "Data Source") {
                +"Lyrical uses the Spotify API and Genius API to get song data, and scrapes the Genius website to get lyrics"
            }
            AboutCard(Icon.Search, "Iconography") {
                +"Lyrical uses "
                Link(href = "https://material.io/icons") { +"Material Design Icons" }
                +" for most of the icons in the app, with "
                Link(href = "http://evericons.com") { +"Evericons" }
                +" for the rest"
            }
            AboutCard(Icon.Typography, "Typography") {
                +"Lyrical uses two open source fonts - "
                Link(href = "https://rsms.me/inter/") { +"Inter" }
                +" for body styles.text and "
                Link(href = "https://github.com/noirblancrouge/YoungSerif") { +"Young Serif" }
                +" for headers"
            }
        }
    }
}

private fun RBuilder.AboutCard(icon: Icon, title: String, description: Builder<RElementBuilder<TextProps>> = {}) {
    HStack({
        layerStyle = "card"
        spacing = arrayOf(24, 32)
        alignItems = "start"
    }) {
        Circle({ boxSize = arrayOf(40, 56, 64); bgColor = ChakraTheme.background }) {
            Icon(icon)
        }
        VStack({ spacing = 0; width = "100%"; alignItems = "start" }) {
            Subtitle1 { +title }
            SectionHeader { description.invoke(this) }
        }
    }
}