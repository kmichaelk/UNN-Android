package io.github.kmichaelk.unnandroid.ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kmichaelk.unnandroid.R
import io.github.kmichaelk.unnandroid.ui.state.UiError

@Composable
fun FancyError(
    title: String,
    message: String,
    onRetry: (() -> Unit)? = null,
) {
    FancyNotice(painter = painterResource(id = R.drawable.error)) {
        Text(
            title,
            fontWeight = FontWeight.Light,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            message,
            fontSize = 14.sp,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        onRetry?.let {
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = it) {
                Text("Повторить")
            }
        }
    }
}

@Composable
fun FancyError(error: UiError, onRetry: (() -> Unit)? = null) = FancyError(
    title = error.title,
    message = error.message,
    onRetry = onRetry,
)