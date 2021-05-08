package ui.setup

import LyricsRepository
import ui.khakra.Heading1
import SetupAction
import VerticalSpacing
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import com.github.mpetuska.khakra.button.Button
import com.github.mpetuska.khakra.hooks.useDisclosure
import com.github.mpetuska.khakra.image.Image
import com.github.mpetuska.khakra.kt.set
import com.github.mpetuska.khakra.layout.*
import com.github.mpetuska.khakra.transition.Collapse
import flexbox
import getClientAPIIfLoggedIn
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import logout
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import react.*
import styled.*
import toSourcedTracks
import ui.common.Icon
import ui.demo.PlaylistExport
import ui.khakra.Subtitle1
import ui.khakra.onClick

fun RBuilder.SetupScreen(state: State.Setup, onUpdateSetup: (SetupAction) -> Unit) = child(setup) {
    attrs {
        this.state = state
        this.onUpdateSetup = onUpdateSetup
    }
}

external interface SetupProps : RProps {
    var state: State.Setup
    var onUpdateSetup: (SetupAction) -> Unit
}

val setup = functionalComponent<SetupProps> { props ->
    Container({ maxW = "1280px"; p = arrayOf("32", "48", "64") }) {
        Stack({ spacing = arrayOf("32", "48", "64") }) {
            AppHeader(props.state.selectedPlaylists.isNotEmpty() && props.state.config.amountOfSongs > 0) {
                props.onUpdateSetup.invoke(SetupAction.StartGame(props.state.selectedPlaylists, props.state.config))
            }
            Stack({ direction = arrayOf("column", "column", "row"); spacing = arrayOf("32", "48", "64") }) {
                Sidebar(props.state, props.onUpdateSetup)
                FindPlaylists(
                    props.state.addPlaylistState,
                    onAction = { props.onUpdateSetup.invoke(it) }
                )
            }
        }
    }
/*    Screen {
        css {
            gap = Gap("64px")
            justifyContent = JustifyContent.start
        }
    }*/
}


private fun RBuilder.AppHeader(canStartGame: Boolean, onPlayGameClick: () -> Unit) {
    flexbox(justifyContent = JustifyContent.spaceBetween, alignItems = Align.center) {
        css { width = 100.pct }
        HStack({ spacing = arrayOf(16, 24, 32) }) {
            Image({
                src = "/assets/LyricalIcon.svg"
                boxSize = arrayOf("40", "48", "64")
            })
            Heading1 { +"Lyrical" }
        }
        Button({
            isDisabled = !canStartGame
            onClick = {
                onPlayGameClick.invoke()
            }
            size = "fab"
        }) {
            +"Play Game".toUpperCase()
        }
    }
}

private fun RBuilder.Sidebar(setupState: State.Setup, onUpdateSetup: (SetupAction) -> Unit) {
    VStack({
        spacing = arrayOf("8", "16", "32")
        w = arrayOf("100%", "100%", "30%")
        minWidth = arrayOf("0px", "0px", "350px", "400px")
        h = "min-content"
        position = arrayOf("static", "static", "sticky")
        top = 0
        marginTop = arrayOf(0, 0, -64)
        paddingTop = arrayOf(0, 0, 64)
    }) {
        VStack({
            layerStyle = "primaryCard"
            width = "100%"
            alignItems = "stretch"
            spacing = 0
        }) {
            val disclosure = useDisclosure()
            useEffect(dependencies = listOf(setupState.selectedPlaylists)) {
                if (setupState.selectedPlaylists.isEmpty()) disclosure.onClose.invoke() else disclosure.onOpen.invoke()
            }
            SectionHeader("${setupState.selectedPlaylists.size} Playlists Selected", disclosure.isOpen, enabled = setupState.selectedPlaylists.isNotEmpty()) { if (setupState.selectedPlaylists.isNotEmpty()) disclosure.onToggle() }
            Collapse({
                `in` = disclosure.isOpen
                this["marginTop"] = "0px"
            }) {
                VerticalSpacing("24", "32")
                VStack({
                    w = "100%"
                    spacing = "16"
                }) {
                    setupState.selectedPlaylists.forEach {
                        HorizontalPlaylistItem(it, true) {
                            onUpdateSetup.invoke(SetupAction.RemovePlaylist(it))
                        }
                    }
                    if (BuildConfig.debug) {
                        Button({
                            size = "fabStatic"
                            variant = "solidCard"
                            onClick = {
                                CoroutineScope(Dispatchers.Default).launch {
                                    val api = getClientAPIIfLoggedIn {  } ?: return@launch
                                    val lyricsRepository = LyricsRepository()
                                    val serialized = Json.Default.encodeToString<List<PlaylistExport>>(ListSerializer(PlaylistExport.serializer()), setupState.selectedPlaylists.map { playlist: SimplePlaylist ->
                                        val tracks = api.playlists.getPlaylistTracks(playlist.id).getAllItemsNotNull().map { it.track }.filterIsInstance<Track>()
                                        val sourcedTracks = tracks.toSourcedTracks(playlist)
                                        PlaylistExport(playlist.name, lyricsRepository.getLyricsFor(sourcedTracks))
                                    })
                                    val blob = Blob(arrayOf(serialized))
                                    val url = URL.createObjectURL(blob)
                                    window.location.href = url
                                }
                            }
                        }) {
                            +"Export Playlists"
                        }
                    }
                }
            }
        }
        VStack({
            layerStyle = "card"
            width = "100%"
            alignItems = "stretch"
            spacing = 0
        }) {
            val disclosure = useDisclosure()
            SectionHeader("Options", disclosure.isOpen) { disclosure.onToggle() }
            Collapse({
                `in` = disclosure.isOpen
                this["mt"] = "0"
            }) {
                VerticalSpacing("24", "32")
                VStack({ spacing = arrayOf("24", "32") }) {
                    GameOptions(setupState.config) {
                        onUpdateSetup.invoke(SetupAction.UpdateConfig(it))
                    }
                    Divider()
                    AppOptions {
                        logout()
                    }
                }
            }
        }
    }
}

private fun RBuilder.SectionHeader(title: String, open: Boolean, enabled: Boolean = true, textColor: String = "inherit", onToggle: () -> Unit) {
    HStack({
        `as` = "Button"
        this["variant"] = "link"
        width = "100%"
        spacing = arrayOf(8, 12, 16)
        onClick = onToggle
    }) {
        Subtitle1({ width = "100%"; textAlign = "left"; opacity = if (enabled) 1f else .5f }, textColor = textColor) { +title }
        Icon(Icon.Arrow.Right, color = textColor) {
            flexShrink = 0
            transition = "transform 200ms"
            transform = if (open) "rotate(90deg)" else "rotate(0deg)"
            opacity = if (enabled) 1f else .2f
        }
    }
}