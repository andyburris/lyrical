package com.andb.apps.lyrical.pages.dataexplorer.withdata

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.andb.apps.lyrical.components.widgets.Body
import com.andb.apps.lyrical.components.widgets.Button
import com.andb.apps.lyrical.components.widgets.SegmentedControl
import com.andb.apps.lyrical.components.widgets.SegmentedControlItem
import com.andb.apps.lyrical.pages.dataexplorer.data.*
import com.andb.apps.lyrical.theme.LyricalTheme
import com.andb.apps.lyrical.theme.onSubmit
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import org.jetbrains.compose.web.dom.Text

private enum class FilterType {
    Group, Sort, View
}

@Composable
fun FilterSelector(
    filters: Filters,
    modifier: Modifier = Modifier,
    onChangeFilters: (Filters) -> Unit,
) {
    val selectedFilter = remember { mutableStateOf<FilterType?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .gap(LyricalTheme.Spacing.MD)
    ) {
        Row(
            modifier = Modifier.gap(LyricalTheme.Spacing.MD),
        ) {
            FilterType.entries.forEach {
                Button(
                    text = it.name,
                    modifier = Modifier.onSubmit {
                        selectedFilter.value = if (selectedFilter.value == it) null else it
                    }
                )
            }
        }

        when(selectedFilter.value) {
            FilterType.Group -> GroupFilterSelector(
                groupFilter = filters.groupFilter,
                combineFilter = filters.combineFilter,
                onChangeGroupFilter = { onChangeFilters(filters.copy(groupFilter =  it)) },
                onChangeCombineFilter = { onChangeFilters(filters.copy(combineFilter = it)) },
            )
            FilterType.Sort -> SortSelector(
                sortFilter = filters.sortFilter,
                hasGroups = filters.groupFilter.groupBy != GroupType.None,
                onChangeSortFilter = { onChangeFilters(filters.copy(sortFilter = it)) }
            )
            FilterType.View -> ViewSelector(
                viewFilter = filters.viewFilter,
                onChangeViewFilter = { onChangeFilters(filters.copy(viewFilter = it)) }
            )
            null -> {}
        }

    }
}

@Composable
private fun GroupFilterSelector(
    groupFilter: GroupFilter,
    combineFilter: CombineFilter,
    modifier: Modifier = Modifier,
    onChangeGroupFilter: (GroupFilter) -> Unit,
    onChangeCombineFilter: (CombineFilter) -> Unit,
) {
    Body("Group by", color = LyricalTheme.palette.contentSecondary)
    SegmentedControl(
        options = GroupType.entries.toList(),
        item = { groupType ->
            SegmentedControlItem(
                item = groupType,
                isSelected = groupFilter.groupBy == groupType,
                modifier = Modifier.onSubmit {
                    val newFilters = groupFilter.copy(groupBy = groupType)
                    onChangeGroupFilter(newFilters)
                },
                content = { Text(it.name) }
            )
        }
    )
    Body("Combine by", color = LyricalTheme.palette.contentSecondary)
    SegmentedControl(
        options = CombineType.entries.toList(),
        item = { combineType ->
            SegmentedControlItem(
                item = combineType,
                isSelected = combineFilter.combineType == combineType,
                modifier = Modifier.onSubmit {
                    val newFilters = combineFilter.copy(combineType = combineType)
                    onChangeCombineFilter(newFilters)
                },
                content = { Text(it.name) }
            )
        }
    )
    if (groupFilter.groupBy != GroupType.None) {
        SegmentedControl(
            options = listOf(false, true),
            item = { combineAcrossGroups ->
                SegmentedControlItem(
                    item = combineAcrossGroups,
                    isSelected = combineFilter.combineAcrossGroups == combineAcrossGroups,
                    modifier = Modifier.onSubmit {
                        val newFilters = combineFilter.copy(combineAcrossGroups = combineAcrossGroups)
                        onChangeCombineFilter(newFilters)
                    },
                    content = { Text(if(it) "Across Groups" else "Within Groups") }
                )
            }
        )
    }
    SegmentedControl(
        options = CombineInto.entries.toList(),
        item = { combineInto ->
            SegmentedControlItem(
                item = combineInto,
                isSelected = combineFilter.combineInto == combineInto,
                modifier = Modifier.onSubmit {
                    val newFilters = combineFilter.copy(combineInto = combineInto)
                    onChangeCombineFilter(newFilters)
                },
                content = { Text(it.name) }
            )
        }
    )
}

@Composable
fun SortSelector(
    sortFilter: SortFilter,
    hasGroups: Boolean,
    modifier: Modifier = Modifier,
    onChangeSortFilter: (SortFilter) -> Unit,
) {
    if (hasGroups) {
        Body("Sort groups by", color = LyricalTheme.palette.contentSecondary)
        SegmentedControl(
            options = GroupSortType.entries.toList(),
            item = { groupSortType ->
                SegmentedControlItem(
                    item = groupSortType,
                    isSelected = sortFilter.groupSortType == groupSortType,
                    modifier = Modifier.onSubmit {
                        val newSortFilter = sortFilter.copy(groupSortType = groupSortType)
                        onChangeSortFilter(newSortFilter)
                    },
                    content = { Text(it.name) }
                )
            }
        )
        SegmentedControl(
            options = listOf(false, true),
            item = { sortGroupsAscending ->
                SegmentedControlItem(
                    item = sortGroupsAscending,
                    isSelected = sortFilter.sortGroupsAscending == sortGroupsAscending,
                    modifier = Modifier.onSubmit {
                        val newSortFilter = sortFilter.copy(sortGroupsAscending = sortGroupsAscending)
                        onChangeSortFilter(newSortFilter)
                    },
                    content = { isAscending ->
                        val text = when(sortFilter.groupSortType) {
                            GroupSortType.Date -> if(isAscending) "Oldest to newest" else "Newest to oldest"
                            GroupSortType.Plays -> if (isAscending) "Least to most plays" else "Most to least plays"
                        }
                        Text(text)
                    }
                )
            }
        )
    }
    Body("Sort items by", color = LyricalTheme.palette.contentSecondary)
    SegmentedControl(
        options = ItemSortType.entries.toList(),
        item = { itemSortType ->
            SegmentedControlItem(
                item = itemSortType,
                isSelected = sortFilter.itemSortType == itemSortType,
                modifier = Modifier.onSubmit {
                    val newSortFilter = sortFilter.copy(itemSortType = itemSortType)
                    onChangeSortFilter(newSortFilter)
                },
                content = { Text(it.name) }
            )
        }
    )
    SegmentedControl(
        options = listOf(false, true),
        item = { sortItemsAscending ->
            SegmentedControlItem(
                item = sortItemsAscending,
                isSelected = sortFilter.sortItemsAscending == sortItemsAscending,
                modifier = Modifier.onSubmit {
                    val newSortFilter = sortFilter.copy(sortItemsAscending = sortItemsAscending)
                    onChangeSortFilter(newSortFilter)
                },
                content = { isAscending ->
                    val text = when(sortFilter.itemSortType) {
                        ItemSortType.Date -> if(isAscending) "Oldest to newest" else "Newest to oldest"
                        ItemSortType.Plays -> if (isAscending) "Least to most plays" else "Most to least plays"
                        ItemSortType.Name, ItemSortType.ArtistName -> if (isAscending) "a-z" else "z-a"
                    }
                    Text(text)
                }
            )
        }
    )
}

@Composable
fun ViewSelector(
    viewFilter: ViewFilter,
    modifier: Modifier = Modifier,
    onChangeViewFilter: (ViewFilter) -> Unit,
) {
    Body("Primary info", color = LyricalTheme.palette.contentSecondary)
    SegmentedControl(
        options = ViewInfoType.entries.toList(),
        item = { primaryViewInfo ->
            SegmentedControlItem(
                item = primaryViewInfo,
                isSelected = viewFilter.primaryViewInfo == primaryViewInfo,
                modifier = Modifier.onSubmit {
                    val newViewFilter = viewFilter.copy(primaryViewInfo = primaryViewInfo)
                    onChangeViewFilter(newViewFilter)
                },
                content = { Text(it.name) }
            )
        }
    )
    Body("Also show", color = LyricalTheme.palette.contentSecondary)
    SegmentedControl(
        options = ViewInfoType.entries.toList() + null,
        item = { secondaryViewInfo ->
            SegmentedControlItem(
                item = secondaryViewInfo,
                isSelected = viewFilter.secondaryViewInfo == secondaryViewInfo,
                modifier = Modifier.onSubmit {
                    val newViewFilter = viewFilter.copy(secondaryViewInfo = secondaryViewInfo)
                    onChangeViewFilter(newViewFilter)
                },
                content = {
                    Text(it?.name ?: "None")
                }
            )
        }
    )
}