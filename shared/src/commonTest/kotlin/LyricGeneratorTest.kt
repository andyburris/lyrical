import kotlin.test.Test
import kotlin.test.assertEquals

class LyricGeneratorTest {
    @Test
    fun testRepeatingLines() {
        """
            Give me everything tonight (Hey)
            Give me everything tonight (Hey)
            Give me everything tonight (Hey)
            Give me everything tonight
        """.trimIndent() generatesTo 2
        """
            Give me everything tonight (Hey)
            Give me everything tonight (Hey)
            Give me everything tonight (Hey)
            Give me everything tonight
            Give me everything tonight
            Take advantage of tonight (Yeah)
        """.trimIndent() generatesTo 4

    }
}

private infix fun String.generatesTo(index: Int) = assertEquals(index, this.lines().randomLyricIndex(Difficulty.Hard, title = "lksdjfklsjfsadjf;lksfd;l"))