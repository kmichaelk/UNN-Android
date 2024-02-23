package io.github.kmichaelk.unnandroid.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedComment
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedUser
import io.github.kmichaelk.unnandroid.ui.AppScreen
import io.github.kmichaelk.unnandroid.ui.LocalNavController

@Composable
fun PostComment(
    comment: PortalFeedComment,
    onUserOpen: (PortalFeedUser) -> Unit
) {
    val navController = LocalNavController.current
    val uriHandler = LocalUriHandler.current

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { onUserOpen(comment.author) }
                    .padding(12.dp),
            ) {
                Box(Modifier.size(48.dp)) {
                    comment.author.avatarUrl?.let {
                        AsyncImage(
                            model = PortalService.BASE_URL + it,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } ?: DummyAvatar(Modifier.fillMaxSize())
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(comment.author.name, fontSize = 14.sp)
                    Text(comment.datetime, fontSize = 12.sp, lineHeight = 12.sp, fontWeight = FontWeight.Light)
                }
            }
            Spacer(Modifier.height(4.dp))

            Box(Modifier.padding(horizontal = 12.dp)) {
                HtmlText(comment.html, onClicked = {
                    try {
                        uriHandler.openUri(it)
                    } catch (ignore: Exception) {}
                })
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}