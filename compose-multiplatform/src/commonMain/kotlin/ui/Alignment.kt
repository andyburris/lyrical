package compose.multiplatform.ui

interface Alignment {
    interface Vertical : Alignment
    interface Horizontal : Alignment

    companion object {
        val TopStart = object : Alignment {}
        val TopCenter = object : Alignment {}
        val TopEnd = object : Alignment {}
        val CenterStart = object : Alignment {}
        val Center = object : Alignment {}
        val CenterEnd = object : Alignment {}
        val BoottomStart = object : Alignment {}
        val BoottomCenter = object : Alignment {}
        val BoottomEnd = object : Alignment {}

        val Top = object : Vertical {}
        val CenterVertically = object : Vertical {}
        val Bottom = object : Vertical {}

        val Start = object : Horizontal {}
        val CenterHorizontally = object : Horizontal {}
        val End = object : Horizontal {}
    }
}
