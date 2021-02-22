fun List<String>.randomLyricIndex(difficulty: Difficulty): Int {
    val byFrequency: Map<String, List<IndexedValue<String>>> = this.dropLast(1).withIndex().groupBy { it.value.withoutAdlibs() }
    println("byFrequency = $byFrequency")

    val selectedLyric = when (difficulty) {
        Difficulty.Easy -> byFrequency.filterWordCount(4).filterFrequency { it > 1 }
        Difficulty.Medium -> byFrequency.filterWordCount(4)
        Difficulty.Hard -> byFrequency.filterWordCount(3).filterFrequency { it == 1 }
    }.toList().random().second.filterNextNotRepeating().random()

    println("selectedLyric = $selectedLyric")
    return selectedLyric.index
}

private fun Map<String, List<IndexedValue<String>>>.filterFrequency(block: (Int) -> Boolean) = if (this.any { block(it.value.size) }) this.filter { block(it.value.size) } else this
private fun Map<String, List<IndexedValue<String>>>.filterWordCount(minWordCount: Int): Map<String, List<IndexedValue<String>>> {
    (minWordCount downTo 0).map { wordCount ->
        if (this.any { it.key.uniqueWordCount() > wordCount }) return this.filter { it.key.uniqueWordCount() >= wordCount }
    }
    return this
}

private fun List<IndexedValue<String>>.filterNextNotRepeating() = this.filter { indexedLyric -> indexedLyric.index + 1 !in this.map { it.index } }

fun String.uniqueWordCount() = this.words().distinctBy { it.toLowerCase() }.size
fun String.words() = this.split("\\s+".toRegex()).map { word ->
    word.replace("""^[,\.]|[,\.]$""".toRegex(), "")
}
fun String.withoutAdlibs() = this.replace("""(\(.*\))|(\[.*\])""".toRegex(), "").trim()