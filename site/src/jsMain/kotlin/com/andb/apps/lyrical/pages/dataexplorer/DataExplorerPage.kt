package com.andb.apps.lyrical.pages.dataexplorer

import androidx.compose.runtime.*
import com.andb.apps.lyrical.components.layouts.PageLayout
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.pages.dataexplorer.data.*
import com.andb.apps.lyrical.pages.dataexplorer.loaddata.LoadData
import com.andb.apps.lyrical.pages.dataexplorer.withdata.WithData
import com.juul.indexeddb.Database
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.coroutines.launch


@Page("/data-explorer")
@Composable
fun DataExplorerPage() {
    val coroutineScope = rememberCoroutineScope()
    val (database, setDatabase) = remember { mutableStateOf<Database?>(null) }
    val (loadedData, setLoadedData) = remember { mutableStateOf<List<AudioHistoryEntry>?>(null) }
    LaunchedEffect(Unit) {
        val loadedDatabase = initializeDatabase()
        setDatabase(loadedDatabase)
        setLoadedData(loadedDatabase.loadEntries())
    }
    val router = rememberPageContext().router
    PageLayout("Data Explorer") {
        when(database) {
            null -> {
                Heading2("Loading...")
            }
            else -> when(loadedData?.size) {
                null -> {}
                0 -> LoadData(
                    onLoad = { loadedEntries, rememberData ->
                        println("loadedEntries size = ${loadedEntries.size}")
                        println("loadedEntries = ${loadedEntries}")
                        setLoadedData(loadedEntries)
                        if (rememberData != null) coroutineScope.launch { database.saveEntries(loadedEntries) }
                    }
                )
                else -> WithData(
                    data = loadedData,
                    onClearData = {
                        coroutineScope.launch { database.clearEntries() }
                        router.navigateTo("/data-explorer")
                    }
                )
            }
        }
    }
}

