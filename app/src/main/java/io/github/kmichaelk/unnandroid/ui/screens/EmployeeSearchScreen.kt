package io.github.kmichaelk.unnandroid.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.ui.AppScreen
import io.github.kmichaelk.unnandroid.ui.LocalNavController
import io.github.kmichaelk.unnandroid.ui.composables.DummyAvatar
import io.github.kmichaelk.unnandroid.ui.viewmodels.EmployeeSearchScreenViewModel

@Composable
fun EmployeeSearchScreen(
    viewModel: EmployeeSearchScreenViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    UserSearchScreen(
        viewModel = viewModel,
        title = "Поиск по сотрудникам"
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate("${AppScreen.User.name}/${it.id}")
                }
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(Modifier.size(48.dp)) {
                        if (it.avatar.isAvailable()) {
                            AsyncImage(
                                model = PortalService.BASE_URL + it.avatar.urlThumbnail,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        } else {
                            DummyAvatar(Modifier.fillMaxSize())
                        }
                    }
                    Column(Modifier.weight(1f)) {
                        Text(it.name, fontWeight = FontWeight.SemiBold)
                    }

                }

                Spacer(Modifier.height(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    it.profiles.map {
                        Column(Modifier.fillMaxWidth()) {
                            Text(it.department!![it.department.size - 1].title, fontSize = 15.sp)
                            Text(it.jobTitle!!, fontSize = 14.sp, fontWeight = FontWeight.Light)
                        }
                    }
                }
            }
        }
    }
}