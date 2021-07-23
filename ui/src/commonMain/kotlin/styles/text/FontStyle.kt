package styles.text


/**
 *  Defines whether the font is [Italic] or [Normal].
 *
 *  @see Font
 *  @see FontFamily
 */
@Suppress("INLINE_CLASS_DEPRECATED")
inline class FontStyle(val value: Int) {

    override fun toString(): String {
        return when (this) {
            Normal -> "Normal"
            Italic -> "Italic"
            else -> "Invalid"
        }
    }

    companion object {
        /** Use the upright glyphs */
        val Normal = FontStyle(0)

        /** Use glyphs designed for slanting */
        val Italic = FontStyle(1)

        /** Returns a list of possible values of [FontStyle]. */
        fun values(): List<FontStyle> = listOf(Normal, Italic)
    }
}
