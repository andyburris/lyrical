package com.andb.apps.lyrical.pages.dataexplorer.withdata

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.andb.apps.lyrical.components.widgets.Button
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.components.widgets.SegmentedControl
import com.andb.apps.lyrical.components.widgets.SegmentedControlItem
import com.andb.apps.lyrical.pages.dataexplorer.common.LogoMark
import com.andb.apps.lyrical.pages.dataexplorer.data.*
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.onSubmit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import kotlinx.coroutines.promise
import org.jetbrains.compose.web.dom.Text
import web.workers.Worker

@Composable
fun WithData(
    data: List<AudioHistoryEntry>,
    modifier: Modifier = Modifier,
    onClearData: () -> Unit,
) {
    val (filters, setFilters) = remember { mutableStateOf(Filters()) }
    val grouped = remember(data, filters.sortFilter, filters.groupFilter, filters.combineFilter) { mutableStateOf<List<Group>?>(null) }
    LaunchedEffect(data, filters.sortFilter, filters.groupFilter, filters.combineFilter) {
        this.promise {
            data.withFilters(filters)
        }.then {
            grouped.value = it
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .gap(LyricalTheme.Spacing.LG),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gap(LyricalTheme.Spacing.MD),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            LogoMark()
            Button("Clear Data", Modifier.onClick { onClearData() })
        }

        FilterSelector(filters, onChangeFilters = setFilters)

        when(val grouped = grouped.value) {
            null -> Heading2("Loading...")
            else -> DataTable(
                groups = grouped,
                viewInfo = filters.viewFilter,
                modifier = Modifier
                    .fillMaxSize()
                    .gap(LyricalTheme.Spacing.MD)
            )
        }
    }
}