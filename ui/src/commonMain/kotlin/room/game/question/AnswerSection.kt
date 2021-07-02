package room.game.question

import UserAnswer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import jetbrains.compose.common.shapes.CircleShape
import org.jetbrains.compose.common.foundation.layout.Column
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.ui.Modifier
import org.jetbrains.compose.common.ui.background
import org.jetbrains.compose.common.ui.draw.clip
import org.jetbrains.compose.common.ui.padding
import org.jetbrains.compose.common.ui.unit.dp
import platform.Button
import platform.LyricalTheme
import platform.RectangleShape
import platform.Text

@Composable
fun AnswerSection(modifier: Modifier = Modifier, onAnswer: (UserAnswer) -> Unit) {
    Column(modifier) { //TODO: Add Arrangement.SpacedBy(24.dp)
        val answerInput = remember { mutableStateOf("") }
        Column {
            Text("Answer".uppercase(), style = LyricalTheme.typography.subtitle1, color = LyricalTheme.colors.onBackgroundSecondary)
            //TODO: TextInput w/ fillMaxWidth()
        }
        CombinedButton(
            text = {
                Text("Answer")
            },
            isTextEnabled = answerInput.value.isNotBlank(),
            onClickText = { onAnswer.invoke(UserAnswer.Answer(answerInput.value)) },
            icon = {
                //TODO: add Skip Icon
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
        Button(isEnabled = isTextEnabled, onClick = onClickText, shape = RectangleShape) { //TODO: add weight(1f) modifier, colors, etc.
            text()
        }
        Button(isEnabled = isIconEnabled, onClick = onClickIcon, shape = RectangleShape) {
            icon()
        }
    }
}