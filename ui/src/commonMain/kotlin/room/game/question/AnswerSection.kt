package room.game.question

import UserAnswer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import common.Icon
import compose.multiplatform.foundation.Text
import compose.multiplatform.foundation.layout.Column
import compose.multiplatform.foundation.layout.Row
import compose.multiplatform.ui.Modifier
import compose.multiplatform.ui.shape.CircleShape
import org.jetbrains.compose.common.ui.draw.clip
import platform.*

@Composable
fun AnswerSection(modifier: Modifier = Modifier, onAnswer: (UserAnswer) -> Unit) {
    Column(modifier) { //TODO: Add Arrangement.SpacedBy(24.dp)
        val answerInput = remember { mutableStateOf("") }
        Column {
            Text("Answer".uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onBackgroundSecondary)
            TextField(answerInput.value, onValueChange = answerInput.component2(), textStyle = LyricalTheme.typography.h1)
        }
        CombinedButton(
            text = {
                Text("Answer")
            },
            isTextEnabled = answerInput.value.isNotBlank(),
            onClickText = { onAnswer.invoke(UserAnswer.Answer(answerInput.value)) },
            icon = {
                Icon(icon = Icon.Skip, contentDescription = "Skip question")
            },
            onClickIcon = { onAnswer.invoke(UserAnswer.Skipped) }
        )
    }
}

@Composable
private fun CombinedButton(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
    isTextEnabled: Boolean = true,
    onClickText: () -> Unit,
    icon: @Composable () -> Unit,
    isIconEnabled: Boolean = true,
    onClickIcon: () -> Unit,
) {
    Row(modifier.clip(CircleShape)) {
        Button(
            isEnabled = isTextEnabled,
            onClick = onClickText,
            //shape = RectangleShape TODO: readd once shapes are supported
        ) { //TODO: add weight(1f) modifier, colors, etc.
            text()
        }
        FloatingActionButton(
            isEnabled = isIconEnabled,
            onClick = onClickIcon,
            palette = CurrentPalette.invert().copy(background = CurrentPalette.invert().backgroundDark)
            //shape = RectangleShape TODO: readd once shapes are supported
        ) {
            icon()
        }
    }
}