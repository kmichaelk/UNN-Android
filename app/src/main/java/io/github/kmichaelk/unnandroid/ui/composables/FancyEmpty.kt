package io.github.kmichaelk.unnandroid.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kmichaelk.unnandroid.R

@Composable
fun FancyEmpty(
    text: String = "Ничего нет :("
) {
    FancyNotice(painter = painterResource(id = R.drawable.empty)) {
        Text(
            text,
            fontWeight = FontWeight.Light,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}