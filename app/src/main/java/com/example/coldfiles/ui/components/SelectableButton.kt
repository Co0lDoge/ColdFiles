package com.example.coldfiles.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coldfiles.ui.theme.ColdFilesTheme

@Composable
/** Button that can be selected or not **/
fun SelectableButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.outlinedShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    elevation: ButtonElevation? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable (RowScope.() -> Unit)
) {
    val border = if (!selected) ButtonDefaults.outlinedButtonBorder
                 else BorderStroke(
                     width = 1.dp,
                     color = ButtonDefaults.outlinedButtonColors().contentColor
                 )

    OutlinedButton(
        onClick,
        modifier,
        enabled,
        shape,
        colors,
        elevation,
        border = border,
        contentPadding,
        interactionSource,
        content
    )
}

@Composable
@Preview(showBackground = true)
private fun SelectableButtonPreviewSelected() {
    ColdFilesTheme {
        SelectableButton(
            onClick = {},
            selected = true
        ) {
            Text(text = "Button")
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SelectableButtonPreviewNotSelected() {
    ColdFilesTheme {
        SelectableButton(
            onClick = {},
            selected = false
        ) {
            Text(text = "Button")
        }
    }
}