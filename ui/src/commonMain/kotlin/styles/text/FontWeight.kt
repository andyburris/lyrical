package styles.text

/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * The thickness of the glyphs, in a range of [1, 1000].
 *
 * @param weight Font weight value in the range of [1, 1000]
 *
 * @see Font
 * @see FontFamily
 */
@Immutable
class FontWeight(val weight: Int) : Comparable<FontWeight> {

    companion object {
        /** [Thin] */
        @Stable
        val W100 = FontWeight(100)
        /** [ExtraLight] */
        @Stable
        val W200 = FontWeight(200)
        /** [Light] */
        @Stable
        val W300 = FontWeight(300)
        /** [Normal] / regular / plain */
        @Stable
        val W400 = FontWeight(400)
        /** [Medium] */
        @Stable
        val W500 = FontWeight(500)
        /** [SemiBold] */
        @Stable
        val W600 = FontWeight(600)
        /** [Bold] */
        @Stable
        val W700 = FontWeight(700)
        /** [ExtraBold] */
        @Stable
        val W800 = FontWeight(800)
        /** [Black] */
        @Stable
        val W900 = FontWeight(900)

        /** Alias for [W100] */
        @Stable
        val Thin = W100
        /** Alias for [W200] */
        @Stable
        val ExtraLight = W200
        /** Alias for [W300] */
        @Stable
        val Light = W300
        /** The default font weight - alias for [W400] */
        @Stable
        val Normal = W400
        /** Alias for [W500] */
        @Stable
        val Medium = W500
        /** Alias for [W600] */
        @Stable
        val SemiBold = W600
        /**
         * A commonly used font weight that is heavier than normal - alias for [W700]
         */
        @Stable
        val Bold = W700
        /** Alias for [W800] */
        @Stable
        val ExtraBold = W800
        /** Alias for [W900] */
        @Stable
        val Black = W900

        /** A list of all the font weights. */
        internal val values: List<FontWeight> = listOf(
            W100,
            W200,
            W300,
            W400,
            W500,
            W600,
            W700,
            W800,
            W900
        )
    }

    init {
        require(weight in 1..1000) {
            "Font weight can be in range [1, 1000]. Current value: $weight"
        }
    }

    override operator fun compareTo(other: FontWeight): Int {
        return weight.compareTo(other.weight)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FontWeight) return false
        if (weight != other.weight) return false
        return true
    }

    override fun hashCode(): Int {
        return weight
    }

    override fun toString(): String {
        return "FontWeight(weight=$weight)"
    }
}
