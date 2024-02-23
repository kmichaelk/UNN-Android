package io.github.kmichaelk.unnandroid.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.kmichaelk.unnandroid.R

@Composable
fun FancyLoading(
    painter: Painter = painterResource(id = R.drawable.excited_folder)
) {
    FancyNotice(painter = painter) {
        Text(
            "Загрузка...",
            fontWeight = FontWeight.Light,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}