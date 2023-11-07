package com.andb.apps.lyrical.pages.dataexplorer.loaddata

import androidx.compose.runtime.*
import com.andb.apps.lyrical.components.widgets.Body
import com.andb.apps.lyrical.components.widgets.Button
import com.andb.apps.lyrical.components.widgets.Switch
import com.andb.apps.lyrical.components.widgets.phosphor.*
import com.andb.apps.lyrical.pages.dataexplorer.common.LogoMark
import com.andb.apps.lyrical.pages.dataexplorer.data.AudioHistoryEntry
import com.andb.apps.lyrical.pages.dataexplorer.data.toAudioHistoryEntries
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.onSubmit
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.file.readBytes
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.navigation.Anchor
import com.varabyte.kobweb.silk.components.forms.Input
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.get

@Composable
fun LoadData(
    modifier: Modifier = Modifier,
    onLoad: (List<AudioHistoryEntry>, rememberData: ByteArray?) -> Unit,
) {
    val loadedFile = remember { mutableStateOf<File?>(null) }
    val rememberData = remember { mutableStateOf(false) }
    val isProcessing = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .gap(LyricalTheme.Spacing.XL),
    ) {
        LogoMark()
        Body {
            Text("Don’t have your extended data yet? Go to ")
            Anchor(href = "https://www.spotify.com/us/account/privacy/") {
                Text("https://www.spotify.com/us/account/privacy/")
            }
            Text(" and request your extended streaming history. It usually takes ~2 weeks to receive your data.")
        }
        FileUpload(
            loadedFile = loadedFile.value,
            onLoad = { loadedFile.value = it }
        )
        Row(
            modifier = Modifier
                .gap(LyricalTheme.Spacing.MD)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Body("Remember streaming history")
                Body("This stays on your device, and is never uploaded to any servers", color = LyricalTheme.palette.contentSecondary)
            }
            Switch(rememberData.value, onCheckedChange = { rememberData.value = it })
        }
        Button(
            text = if (isProcessing.value) "Processing..." else "Process data",
            icon = if (isProcessing.value) { { PhSpinner() } } else null,
            isEnabled = loadedFile.value != null,
            palette = LyricalTheme.Colors.accentPalette,
            modifier = Modifier
                .onSubmit(isEnabled = loadedFile.value != null, isButton = true) {
                    if (!isProcessing.value) {
                        isProcessing.value = true
                        CoroutineScope(Dispatchers.Default).launch {
                            val file = loadedFile.value ?: return@launch
                            val bytes = file.readBytes()
                            onLoad(bytes.toAudioHistoryEntries(), if(rememberData.value) bytes else null)
                            isProcessing.value = false
                        }
                    }
                }
        )

    }
}

private const val FileInputId = "data-explorer-file-input"
@Composable
fun FileUpload(
    loadedFile: File?,
    modifier: Modifier = Modifier,
    onLoad: (File) -> Unit,
) {
    val inputRef = remember { mutableStateOf<HTMLInputElement?>(null) }
    val loadedFilePath = remember { mutableStateOf("") }
    Label(
        forId = FileInputId,
        attrs = modifier
            .border(1.px, LineStyle.Dashed, LyricalTheme.palette.contentSecondary)
            .borderRadius(LyricalTheme.Radii.MD)
            .cursor(Cursor.Pointer)
            .padding(LyricalTheme.Spacing.LG)
            .fillMaxWidth()
            .toAttrs()
    ) {
        Column(
            modifier = Modifier.gap(LyricalTheme.Spacing.MD),
        ) {
            PhFileArchive(Modifier.color(LyricalTheme.palette.contentSecondary), size = LyricalTheme.Size.Icon.Large, style = IconStyle.REGULAR)
            when(loadedFile) {
                null -> Body("Drag and drop .zip file from export, or click to browse your computer")
                else -> Body(loadedFile.name)
            }
        }
    }
    Input(
        type = InputType.File,
        value = "",
        onValueChanged = {  filePath ->
            loadedFilePath.value = filePath
            runCatching { inputRef.value?.files?.get(0)?.let { onLoad(it) } }
        },
        modifier = Modifier
            .display(DisplayStyle.None)
            .attrsModifier{
                id(FileInputId)
            },
        ref = ref { element ->
            inputRef.value = element
        }
    )
}