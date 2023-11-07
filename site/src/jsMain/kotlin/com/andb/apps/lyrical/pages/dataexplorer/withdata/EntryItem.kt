package com.andb.apps.lyrical.pages.dataexplorer.withdata

import androidx.compose.runtime.Composable
import com.andb.apps.lyrical.components.widgets.Body
import com.andb.apps.lyrical.components.widgets.Subtitle
import com.andb.apps.lyrical.pages.dataexplorer.data.Combination
import com.andb.apps.lyrical.pages.dataexplorer.data.GroupType
import com.andb.apps.lyrical.pages.dataexplorer.data.ViewFilter
import com.andb.apps.lyrical.pages.dataexplorer.data.ViewInfoType
import com.andb.apps.lyrical.theme.LyricalTheme
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import js.intl.NumberFormat
import kotlinx.datetime.*
import kotlin.js.Date

@Composable
fun CombinationItem(
    combination: Combination,
    viewInfo: ViewFilter,
    modifier: Modifier = Modifier,
) = when(combination) {
    is Combination.Artist -> ArtistCombinationItem(combination, viewInfo, modifier)
    is Combination.Track -> TrackCombinationItem(combination, viewInfo, modifier)
}

@Composable
fun ArtistCombinationItem(
    combination: Combination.Artist,
    viewInfo: ViewFilter,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Subtitle(combination.name)
        }
        CombinationRight(combination, viewInfo)
    }
}

@Composable
fun TrackCombinationItem(
    combination: Combination.Track,
    viewInfo: ViewFilter,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Subtitle(combination.name)
            Body(combination.artist, color = LyricalTheme.palette.contentSecondary)
        }
        CombinationRight(combination, viewInfo)
    }
}

@Composable
private fun CombinationRight(
    combination: Combination,
    viewInfo: ViewFilter,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .gap(LyricalTheme.Spacing.SM)
            .flexShrink(0),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (viewInfo.secondaryViewInfo != null) {
            Body(
                text = combination.stringFromViewInfo(viewInfo.secondaryViewInfo),
                color = LyricalTheme.palette.contentSecondary,
                modifier = Modifier.textAlign(TextAlign.End),
            )
        }

        PrimaryChip(text = combination.stringFromViewInfo(viewInfo.primaryViewInfo))
    }
}

private fun Combination.stringFromViewInfo(viewInfoType: ViewInfoType): String = when(viewInfoType) {
    ViewInfoType.Date -> this.listens.first().timestamp.format()
    ViewInfoType.Plays -> this.listens.size.toPlaysString()
    ViewInfoType.Playtime -> this.listens
        .sumOf { it.millisecondsPlayed }
        .let { millis ->
            val totalSeconds = millis / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % (3600 * 60)
            return@let "$hours:$minutes:$seconds"
        }
    ViewInfoType.PercentFiltered -> TODO()
}

fun Int.toPlaysString(): String {
    val commaSeparatedNumber = NumberFormat().format(this)
    return "$commaSeparatedNumber play${if (this == 1) "" else "s"}"
}
@Composable
fun PrimaryChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .backgroundColor(LyricalTheme.palette.overlay)
            .borderRadius(LyricalTheme.Radii.Circle)
            .padding(LyricalTheme.Spacing.SM)
    ) {
        Body(text = text)
    }
}

fun LocalDateTime.format(groupType: GroupType = GroupType.None) = this.toInstant(TimeZone.currentSystemDefault()).format(groupType)
fun Instant.format(groupType: GroupType = GroupType.None): String {
    return this
        .toJSDate()
        .toLocaleDateString("en-us", object : Date.LocaleOptions {
            override var day: String? = when(groupType) {
                GroupType.None, GroupType.Day, GroupType.Hour -> "2-digit"
                else -> undefined
            }
            override var era: String? = undefined
            override var formatMatcher: String? = undefined
            override var hour: String? = when(groupType) {
                GroupType.None, GroupType.Hour -> "numeric"
                else -> undefined
            }
            override var hour12: Boolean? = undefined
            override var localeMatcher: String? = "best fit"
            override var minute: String? = when(groupType) {
                GroupType.None -> "2-digit"
                else -> undefined
            }
            override var month: String? = when(groupType) {
                GroupType.Year -> undefined
                GroupType.Month -> "long"
                else -> "numeric"
            }
            override var second: String? = undefined
            override var timeZone: String? = "America/New_York"
            override var timeZoneName: String? = undefined
            override var weekday: String? = undefined
            override var year: String? = when(groupType) {
                GroupType.Month, GroupType.Year -> "numeric"
                else -> "2-digit"
            }
        })
}
