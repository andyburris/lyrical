package com.andb.apps.lyrical.pages.dataexplorer.withdata

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.widgets.Heading2
import com.andb.apps.lyrical.pages.dataexplorer.data.Group
import com.andb.apps.lyrical.pages.dataexplorer.data.GroupType
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding

@Composable
fun GroupHeaderItem(
    group: Group,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = LyricalTheme.Spacing.XL),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Heading2(
            text = when(group.type) {
                GroupType.None -> "All Time"
                else -> group.dateTime.format(group.type)
            },
            modifier = Modifier,
        )
        PrimaryChip(group.items.sumOf { it.listens.size }.toPlaysString())
    }
}