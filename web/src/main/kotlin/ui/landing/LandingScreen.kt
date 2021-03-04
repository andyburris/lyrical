package ui.landing

import ContentColumn
import ui.khakra.Heading1
import Platform
import ScreenPadding
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
import size
import styled.*
import ui.common.Icon
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
    flexColumn {
        flexColumn {

        }
        ScreenPadding {
            css {
                height = 100.vh - 128.px
                maxHeight = 1200.px
                backgroundColor = theme.primary
                paddingBottom = 0.px
                onHorizontalLayout { paddingBottom = 0.px }
            }
            ContentColumn {
                LandingContent(props.onAuthenticate)
            }
        }
        ScreenPadding {
            css {
                backgroundColor = theme.primary
                padding(vertical = 0.px)
                onHorizontalLayout { padding(vertical = 0.px) }
            }
            ContentColumn {
                PreviewContent()
            }
        }
        ScreenPadding {
            ContentColumn {
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
                Heading1 { +"Lyrical" }
            }
            p { +"Get a random lyric from your playlist and guess what song it’s from. Simple." }
        }
        flexColumn(gap = 16.px) {
            styledButton {
                css { backgroundColor = theme.background }
                +"LOG IN WITH SPOTIFY"
                attrs.onClickFunction = {
                    onAuthenticate.invoke(AuthAction.Authenticate(Random.nextInt().toString()))
                }
            }
            styledP {
                css {
                    color = theme.onPrimaryTernary
                    fontSize = 18.px
                    fontWeight = FontWeight.w600
                }
                +"Lyrical only has access to read your username and playlists"
            }
        }
    }
}

private fun RBuilder.PreviewContent() {
    flexColumn(alignItems = Align.center) {
        BrowserPreview()
        flexColumn(justifyContent = JustifyContent.end) {
            css {
                width = 100.vw
            }
            styledDiv {
                css {
                    position = Position.absolute
                    width = 100.vw
                    height = 96.px
                    backgroundColor = Color.black.withAlpha(0.12).blend(theme.backgroundDark)
                    this.put("clip-path", "polygon(0 0, 0% 100%, 100% 100%);")
                }
            }
            styledDiv {
                css {
                    position = Position.absolute
                    width = 100.vw
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
        h1 { +"Download the app" }
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
    styledButton {
        css {
            backgroundColor = if (currentPlatform) theme.primary else theme.backgroundCard
            color = if (currentPlatform) theme.onPrimary else theme.onBackground
        }
        flexRow(alignItems = Align.center, gap = 16.px) {
            Icon(platform.icon)
            +"Download for ${platform.formattedName}"
        }
    }
}