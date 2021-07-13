package data

import java.io.BufferedReader

fun BufferedReader.charSequence(): Sequence<Char> = CharsSequence(this).constrainOnce()

private class CharsSequence(private val reader: BufferedReader) : Sequence<Char> {
    override fun iterator(): Iterator<Char> {
        return object : Iterator<Char> {
            private var nextValue: Char? = null
            private var done = false

            override fun hasNext(): Boolean {
                if (nextValue == null && !done) {
                    nextValue = reader.read().toChar()
                    if (nextValue == null) done = true
                }
                return nextValue != null
            }

            override fun next(): Char {
                if (!hasNext()) {
                    throw NoSuchElementException()
                }
                val answer = nextValue
                nextValue = null
                return answer!!
            }
        }
    }
}
