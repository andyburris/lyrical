import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.selects.DocElement
import it.skrape.skrape

actual suspend fun GeniusRepository.scrapeLyrics(songURL: String): String? {
    return skrape(HttpFetcher) {
        request {
            url = songURL
        }
        extract {
            htmlDocument {
                val lyricsDiv: DocElement = try {
                    findAll(".lyrics").first()
                } catch (e: Exception) {
                    try {
                        findAll(".Lyrics__Container-sc-1ynbvzw-2 jgQsqn").first()
                    } catch (e: Exception) {
                        Error("Parsing failed for $songURL").printStackTrace()
                        return@htmlDocument null
                    }
                }
                lyricsDiv.html.replace(unnecessaryScrapingRegex, "").replace("&amp;", "&").split("<br>").filter { it.isNotBlank() }.map { it.trim() }.joinToString("\n")
            }
        }
    }
}

actual var savedConfig: GameConfig = GameConfig()
actual var savedPlaylistURIs: List<String> = emptyList()