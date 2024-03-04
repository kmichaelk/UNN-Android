package io.github.kmichaelk.unnandroid.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IconText(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    contentDescription: String?,
    size: TextUnit = 16.sp,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = 1,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    fontWeight: FontWeight? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = iconModifier.size(with(
                LocalDensity.current
            ) { size.toDp() }),
            tint = LocalContentColor.current.copy(alpha = 0.64f)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = size,
            fontWeight = fontWeight,
            lineHeight = lineHeight,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}