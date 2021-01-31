import org.w3c.dom.Element
import org.w3c.dom.get
import org.w3c.xhr.XMLHttpRequest

/*
actual suspend fun GeniusRepository.scrapeLyrics(songURL: String): String?  {
    val request = XMLHttpRequest()
    val document = request.get(songURL)
    val lyricsDiv = document.querySelectorAll(".lyrics")[0] ?: document.querySelectorAll(".Lyrics__Container-sc-1ynbvzw-2 jgQsqn")[0] ?: return null
    return (lyricsDiv as Element).innerHTML.replace(unnecessaryScrapingRegex, "").replace("&amp;", "&").split("<br>").filter { it.isNotBlank() }.map { it.trim() }.joinToString("\n")
}
*/

/*
actual fun getKeys(): Keys {

}*/
