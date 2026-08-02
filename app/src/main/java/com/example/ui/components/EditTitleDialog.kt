package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.ShortSlot
import com.example.ui.theme.Ink
import com.example.ui.theme.Inter
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.MutedText
import com.example.ui.theme.Paper
import com.example.ui.theme.SignalRed

@Composable
fun EditTitleDialog(
    slot: ShortSlot,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var titleText by remember { mutableStateOf(slot.title) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .hardShadow(shadowColor = Ink, offsetX = 6.dp, offsetY = 6.dp, shape = WobblyGridShape)
                .background(Paper, WobblyGridShape)
                .border(2.5.dp, Ink, WobblyGridShape)
                .paperGrainOverlay(0.04f)
                .padding(20.dp)
                .testTag("edit_title_dialog")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "EDIT SLOT 0${slot.id} TITLE",
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Paper, RoundedCornerShape(4.dp))
                        .border(1.5.dp, Ink, RoundedCornerShape(4.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = titleText,
                        onValueChange = {
                            if (it.length <= 42) titleText = it
                        },
                        textStyle = TextStyle(
                            fontFamily = Inter,
                            fontSize = 14.sp,
                            color = Ink
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("title_text_input")
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${titleText.length}/42 characters",
                    fontFamily = JetBrainsMono,
                    fontSize = 9.sp,
                    color = MutedText,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.testTag("cancel_edit_button")
                    ) {
                        Text(
                            text = "CANCEL",
                            fontFamily = Inter,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedText
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = { onSave(titleText) },
                        modifier = Modifier.testTag("save_edit_button")
                    ) {
                        Text(
                            text = "SAVE TITLE",
                            fontFamily = Inter,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SignalRed
                        )
                    }
                }
            }
        }
    }
}
