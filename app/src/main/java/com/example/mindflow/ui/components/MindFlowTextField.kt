package com.example.mindflow.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mindflow.ui.theme.mindFlowColors

@Composable
fun MindFlowTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    trailingIconContentDescription: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val colors = MaterialTheme.mindFlowColors

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = colors.textLabel,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.inputPlaceholder
                )
            },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = trailingIcon?.let {
                {
                    IconButton(
                        onClick = { onTrailingIconClick?.invoke() },
                        enabled = onTrailingIconClick != null
                    ) {
                        Icon(
                            imageVector = it,
                            contentDescription = trailingIconContentDescription
                        )
                    }
                }
            },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.input,
                unfocusedContainerColor = colors.input,
                disabledContainerColor = colors.input.copy(alpha = 0.72f),
                focusedBorderColor = colors.inputFocusedBorder,
                unfocusedBorderColor = colors.inputUnfocusedBorder,
                disabledBorderColor = colors.inputUnfocusedBorder.copy(alpha = 0.5f),
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = colors.textLabel,
                unfocusedLeadingIconColor = colors.inputPlaceholder,
                focusedTrailingIconColor = colors.textLabel,
                unfocusedTrailingIconColor = colors.inputPlaceholder,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        )
    }
}
