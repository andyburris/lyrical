import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.andb.apps.lyrical"
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")
            head.add {
                link(rel = "preconnect", href = "https://fonts.googleapis.com")
                link(rel = "preconnect", href = "https://fonts.gstatic.com") { attributes["crossorigin"] = "" }
                link(
                    href = "https://fonts.googleapis.com/css2?family=Inter&display=swap",
                    rel = "stylesheet"
                )
                link(
                    href = "https://fonts.cdnfonts.com/css/young",
                    rel = "stylesheet"
                )
                link(
                    rel = "stylesheet",
                    href = "https://unpkg.com/@phosphor-icons/web@2.0.3/src/bold/style.css",
                    type = "text/css"
                )
            }
        }
    }
}

kotlin {
    configAsKobwebApplication("lyrical")

    @Suppress("UNUSED_VARIABLE") // Suppress spurious warnings about sourceset variables not being used
    sourceSets {
        val commonMain by getting {
            dependencies {

            }
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.html.core)
                implementation(libs.kobweb.core)
                implementation(libs.kobweb.silk.core)
                implementation(libs.kobweb.silk.icons.fa)
                implementation(libs.kobwebx.markdown)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.serialization)
                implementation(libs.spotify.api)
                implementation(project(":shared"))
            }
        }
    }
}

val generatePhosphorIconsTask = tasks.register("generatePhosphorIcons") {
    val srcFile = layout.projectDirectory.file("src/jsMain/kotlin/com/andb/apps/lyrical/components/widgets/phosphor/phosphor-icons.txt")
    val dstFile = layout.projectDirectory.file("src/jsMain/kotlin/com/andb/apps/lyrical/components/widgets/phosphor/PhosphorIcons.kt")

    inputs.files(srcFile, layout.projectDirectory.file("build.gradle.kts"))
    outputs.file(dstFile)

    val iconPascalCaseNames: Map<String, String> = srcFile.asFile
        .readLines()
        .asSequence()
        .map { it.removeSuffix(",") }
        .map { rawName -> "${rawName.split("-").joinToString("") { it.capitalize() }}" to rawName }
        .toMap()
    val methods = iconPascalCaseNames.map { (pascal, kebab) ->
        "@Composable fun Ph$pascal(modifier: Modifier = Modifier, size: CSSNumeric = LyricalTheme.Size.Icon.Default, style: IconStyle = IconStyle.BOLD) = PhIcon(\"ph-$kebab\", modifier, size, style)"
    }

    val code ="""
        //@formatter:off
        @file:Suppress("unused", "SpellCheckingInspection")

        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // THIS FILE IS AUTOGENERATED.
        //
        // Do not edit this file by hand. Instead, update `phosphor-icons.txt` and run the Gradle
        // task "generatePhosphorIcons"
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        package com.andb.apps.lyrical.components.widgets.phosphor
        
        import androidx.compose.runtime.Composable
        import com.andb.apps.lyrical.theme.LyricalTheme
        import com.varabyte.kobweb.compose.ui.Modifier
        import com.varabyte.kobweb.compose.ui.modifiers.fontSize
        import com.varabyte.kobweb.compose.ui.modifiers.size
        import com.varabyte.kobweb.compose.ui.toAttrs
        import org.jetbrains.compose.web.css.CSSNumeric
        import org.jetbrains.compose.web.dom.I
        
        enum class IconStyle {
            THIN,
            LIGHT,
            REGULAR,
            BOLD,
            FILL,
            DUOTONE,
        }

        fun IconStyle.toClassName(): String = when (this) {
            IconStyle.THIN -> "ph-thin"
            IconStyle.LIGHT -> "ph-light"
            IconStyle.REGULAR -> "ph-regular"
            IconStyle.BOLD -> "ph-bold"
            IconStyle.FILL -> "ph-fill"
            IconStyle.DUOTONE -> "ph-duotone"
        }

        @Composable
        fun PhIcon(
            name: String,
            modifier: Modifier,
            size: CSSNumeric,
            style: IconStyle,
        ) {
            I(attrs = modifier.fontSize(size).size(size).toAttrs { classes(name, style.toClassName()) })
        }

${methods.joinToString("\n") { "        $it"} }
    """.trimIndent()

    dstFile.asFile.writeText(code)
}


tasks.named("compileKotlinJs") {
    dependsOn(generatePhosphorIconsTask)
}

tasks.named("jsSourcesJar") {
    dependsOn(generatePhosphorIconsTask)
}