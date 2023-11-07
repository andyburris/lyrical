package com.andb.apps.lyrical.pages.dataexplorer.withdata

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import com.andb.apps.lyrical.pages.dataexplorer.data.Combination
import com.andb.apps.lyrical.pages.dataexplorer.data.Group
import com.andb.apps.lyrical.pages.dataexplorer.data.GroupType
import com.andb.apps.lyrical.pages.dataexplorer.data.ViewFilter
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.toAttrs
import kotlinx.datetime.LocalDateTime
import opensavvy.compose.lazy.LazyColumn

@Composable
fun DataTable(
    groups: List<Group>,
    viewInfo: ViewFilter,
    modifier: Modifier = Modifier,
) {
    val tableItems: List<TableItem> = remember(groups) {
        groups.flatMap { group ->
            listOf(TableItem.Header(group)) + group.items.map { TableItem.Combination(it) }
        }
    }

    key(tableItems, viewInfo) {
        LazyColumn(modifier.toAttrs()) {
            items(tableItems) { item: TableItem ->
                when(item) {
                    is TableItem.Combination -> CombinationItem(item.combination, viewInfo, Modifier.fillMaxWidth())
                    is TableItem.Header -> GroupHeaderItem(item.group)
                }
            }
        }
    }
}

private sealed class TableItem {
    data class Header(val group: Group) : TableItem()
    data class Combination(val combination: com.andb.apps.lyrical.pages.dataexplorer.data.Combination) : TableItem()
}