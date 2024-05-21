package com.kuricki.keyquest.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun RoundedButtonWithIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String
) {
    Button(
        onClick = onClick,
        shape = CircleShape
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}