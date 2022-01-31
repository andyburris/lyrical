package compose.multiplatform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import compose.multiplatform.foundation.Text
import compose.multiplatform.ui.shape.CircleShape
import compose.multiplatform.ui.shape.RoundedCornerShape
import compose.multiplatform.foundation.modifier.padding
import compose.multiplatform.ui.*
import compose.multiplatform.ui.text.FontWeight
import compose.multiplatform.ui.text.TextStyle
import compose.multiplatform.foundation.modifier.background
import compose.multiplatform.foundation.modifier.border
import compose.multiplatform.foundation.modifier.clickable
import compose.multiplatform.foundation.layout.*
import org.jetbrains.compose.common.material.Slider
import org.jetbrains.compose.common.ui.*
import org.jetbrains.compose.common.ui.draw.clip
import compose.multiplatform.ui.unit.dp
import kotlin.random.Random

object LayoutSamples {

    @Composable
    fun DemoCard() {
        val clickCounter = remember { mutableStateOf(0) }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { clickCounter.value = clickCounter.value + 1 }
                .background(Color(29, 185, 84))
                .padding(16.dp)
                .size(width = 300.dp, height = 150.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text("Title", color = Color.White, style = TextStyle(fontWeight = FontWeight.SemiBold))
            Text(
                text = "Summaries are slightly longer text that might wrap multiple lines. This card has been clicked ${clickCounter.value} times.",
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }

    @Composable
    fun TwoTexts() {
        Text("Alfred Sisley")
        Text("3 minutes ago")
    }

    @Composable
    fun TwoTextsInColumn() {
        val defaultFontSize = 0.79f
        val fontSize = remember { mutableStateOf(defaultFontSize) }
        val subtitleColor = remember { mutableStateOf(Color(0, 0, 200)) }
        Column {
            Text("Alfred Sisley")
            Text(
                "3 minutes ago",
                color = subtitleColor.value,
                //size = fontSize.value.em,
                modifier = Modifier.clickable {
                    subtitleColor.value = Color.Yellow
                }
            )
            Slider(
                fontSize.value,
                onValueChange = { value ->
                    fontSize.value = value
                },
                valueRange = 0.1f..1.2f,
                steps = 80,
                modifier = Modifier.width(200.dp)
            )
        }
    }

    @Composable
    fun TwoTextsInRow() {
        Text("Alfred Sisley")
        Text("3 minutes ago")
    }

    @Composable
    fun Layouts() {
        val horizontalArrangements = listOf(Arrangement.Start, Arrangement.Center, Arrangement.End, Arrangement.SpaceAround, Arrangement.SpaceBetween, Arrangement.SpaceEvenly)
        val verticalAlignments = listOf(Alignment.Top, Alignment.CenterVertically, Alignment.Bottom)
        Column() {
            horizontalArrangements.forEach { horizontalArrangement ->
                verticalAlignments.forEach { verticalAlignment ->
                    Row(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(4.dp)
                            .border(1.dp, Color(0, 0, 200))
                            .background(Color.Yellow),
                        horizontalArrangement = horizontalArrangement,
                        verticalAlignment = verticalAlignment
                    ) {
                        Box(Modifier.size(50.dp).background(Color.Red)) { }
                        Box(Modifier.size(30.dp).background(Color.Blue)) { }
                    }
                }
            }
        }
    }

    @Composable
    fun LayoutsClipped() {
        val horizontalArrangements = listOf(Arrangement.Start, Arrangement.End)
        val verticalAlignments = listOf(Alignment.Top, Alignment.CenterVertically, Alignment.Bottom)
        Column() {
            horizontalArrangements.forEach { horizontalArrangement ->
                verticalAlignments.forEach { verticalAlignment ->
                    Row(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(4.dp)
                            .border(1.dp, Color(0, 0, 200))
                            .background(Color.Yellow),
                        horizontalArrangement = horizontalArrangement,
                        verticalAlignment = verticalAlignment
                    ) {
                        Box(Modifier.size(50.dp).background(Color.Red)) { }
                        Box(
                            Modifier
                                .clip(CircleShape)
                                .size(30.dp)
                                .background(Color.Blue)
                        ) { }
                    }
                }
            }
        }
    }

    @Composable
    fun NestedModifiers() {
        val middleColor = remember { mutableStateOf(Color.Blue) }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Red)
                .padding(8.dp)
                .border(4.dp, Color.White)
                .clickable {
                    middleColor.value = Color(Random.nextInt(0, 255), Random.nextInt(0, 255), Random.nextInt(0, 255))
                    println("new middle color = ${middleColor.value}")
                }
                .background(middleColor.value)
                .padding(4.dp)
                .background(Color.Green)
        ) {
            Text("Line 1")
            Text("Line 2")
        }
    }

    @Composable
    fun AlphaTest() {
        Column() {
            Box(Modifier.background(Color(3, 169, 244)).size(64.dp)) {}
            Box(Modifier.background(Color(3, 169, 244, .75f)).size(64.dp)) {}
            Box(Modifier.background(Color(3, 169, 244, .5f)).size(64.dp)) {}
            Box(Modifier.background(Color(3, 169, 244, .25f)).size(64.dp)) {}
            Box(Modifier.background(Color(3, 169, 244, .0f)).size(64.dp)) {}
        }
    }
}

@Composable
fun App() {
    Column(modifier = Modifier.padding(32.dp)) {
        LayoutSamples.DemoCard()
    }

}