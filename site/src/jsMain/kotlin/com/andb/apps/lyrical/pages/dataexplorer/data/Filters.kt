package com.andb.apps.lyrical.pages.dataexplorer.data

import kotlinx.datetime.*
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

data class Filters(
    val groupFilter: GroupFilter = GroupFilter(),
    val sortFilter: SortFilter = SortFilter(),
    val combineFilter: CombineFilter = CombineFilter(),
    val viewFilter: ViewFilter = ViewFilter(),
)

sealed class Groupable {
    abstract val timestamp: Instant
    data class CombineAcrossGroups(val combination: Combination) : Groupable() {
        override val timestamp: Instant = combination.listens.first().timestamp
    }
    data class CombineWithinGroups(val listen: AudioHistoryEntry) : Groupable() {
        override val timestamp: Instant = listen.timestamp
    }
}

fun List<Groupable>.groupWith(groupFilter: GroupFilter, combineFilter: CombineFilter): List<Group> {
    fun List<Groupable>.getCombinations() = when(combineFilter.combineAcrossGroups) {
        true -> this.filterIsInstance<Groupable.CombineAcrossGroups>().map { it.combination }
        false -> this.filterIsInstance<Groupable.CombineWithinGroups>().map { it.listen }.toCombinations(combineFilter)
    }

    return when(val groupBy = groupFilter.groupBy) {
        GroupType.None -> listOf(Group(GroupType.None, dateTime = this.first().timestamp.toLocalDateTime(TimeZone.currentSystemDefault()), this.getCombinations()))
        else -> this.groupBy { it.timestamp.roundTo(groupBy) }.map { (k, v) -> Group(groupBy, k.toLocalDateTime(TimeZone.currentSystemDefault()), v.getCombinations()) }
    }
}

fun List<AudioHistoryEntry>.withFilters(filters: Filters): List<Group> = measureTimedValue {
    val groupables = when(filters.combineFilter.combineAcrossGroups) {
        true -> this.toCombinations(filters.combineFilter).map { Groupable.CombineAcrossGroups(it) }
        false -> this.map { Groupable.CombineWithinGroups(it) }
    }
    val grouped = groupables.groupWith(filters.groupFilter, filters.combineFilter)
    val sorted = grouped
        .sortedBy { group ->
            when(filters.sortFilter.groupSortType) {
                GroupSortType.Date -> group.dateTime.toString()
                GroupSortType.Plays -> group.items.sumOf { it.listens.size }.toString().padStart(15, '0')
            }
        }
        .let { if (filters.sortFilter.sortGroupsAscending) it else it.reversed() }
        .map { group ->
            group.copy(
                items = group.items
                    .sortedBy { combination ->
                        when(filters.sortFilter.itemSortType) {
                            ItemSortType.Date -> combination.listens.first().timestamp.toString()
                            ItemSortType.Plays -> combination.listens.size.toString().padStart(15, '0')
                            ItemSortType.Name -> combination.listens.first().trackName.lowercase()
                            ItemSortType.ArtistName -> combination.listens.first().artistName.lowercase()
                        }
                    }
                    .let { if (filters.sortFilter.sortItemsAscending) it else it.reversed() }
            )
        }
    return@measureTimedValue sorted
}.also { println("filtering took ${it.duration}") }.value

fun Instant.roundTo(groupType: GroupType): Instant = this
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .roundTo(groupType)
    .toInstant(TimeZone.currentSystemDefault())
fun LocalDateTime.roundTo(groupType: GroupType): LocalDateTime = when(groupType) {
    GroupType.None -> throw Error("Can't round this")
    GroupType.Hour -> LocalDate.fromEpochDays(this.date.toEpochDays()).atTime(this.hour, 0, 0)
    GroupType.Day -> LocalDate(this.year, this.month, this.dayOfMonth).atTime(0, 0, 0)
    GroupType.Month -> LocalDate(this.year, this.month.number, 1).atTime(0, 0, 0)
    GroupType.Year -> LocalDate(this.year, 1, 1).atTime(0, 0, 0)
}

private fun List<AudioHistoryEntry>.toCombinations(combineFilter: CombineFilter): List<Combination> = this
    .groupBy { when(combineFilter.combineType) {
        CombineType.None -> it.timestamp.toString()
        CombineType.All -> ""
        CombineType.SameSong -> "${it.trackName} ${it.artistName}"
        CombineType.SameArtist -> it.artistName
    } }
    .map { group ->
        when(combineFilter.combineType) {
            CombineType.SameArtist -> Combination.Artist(group.key, group.value)
            else -> {
                val firstListen = group.value.first()
                val sorted = when(combineFilter.combineInto) {
                    CombineInto.EarliestPlay -> group.value.sortedBy { it.timestamp }
                    CombineInto.LatestPlay -> group.value.sortedByDescending { it.timestamp }
                }
                Combination.Track(firstListen.trackName, firstListen.artistName, sorted)
            }
        }
    }

enum class GroupType {
    None, Hour, Day, Month, Year
}
data class GroupFilter(
    val groupBy: GroupType = GroupType.None,
)
enum class GroupSortType {
    Date, Plays,
}
enum class ItemSortType {
    Date, Plays, Name, ArtistName
}
data class SortFilter(
    val groupSortType: GroupSortType = GroupSortType.Date,
    val sortGroupsAscending: Boolean = true,
    val itemSortType: ItemSortType = ItemSortType.Date,
    val sortItemsAscending: Boolean = true,
)

enum class CombineType {
    None, All, SameSong, SameArtist
}
enum class CombineInto {
    EarliestPlay, LatestPlay
}
data class CombineFilter(
    val combineType: CombineType = CombineType.None,
    val combineAcrossGroups: Boolean = false,
    val combineInto: CombineInto = CombineInto.EarliestPlay,
)

enum class ViewInfoType {
    Date, Plays, Playtime, PercentFiltered,
}
data class ViewFilter(
    val primaryViewInfo: ViewInfoType = ViewInfoType.Plays,
    val secondaryViewInfo: ViewInfoType? = null,
)