package io.github.kmichaelk.unnandroid.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CastForEducation
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FilterFrames
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Source
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.github.kmichaelk.unnandroid.ui.AppScreen
import io.github.kmichaelk.unnandroid.ui.LocalNavController
import io.github.kmichaelk.unnandroid.ui.extensions.popBackStackLifecycleAware
import io.github.kmichaelk.unnandroid.ui.viewmodels.AppDrawerViewModel

@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    content: @Composable (() -> Unit)
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                AppDrawerContent()
            }
        },
        content = content
    )
}

@Composable
fun AppDrawerContent(
    viewModel: AppDrawerViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    val user by viewModel.user.collectAsState()

    Spacer(Modifier.height(16.dp))
    Box(Modifier.fillMaxWidth()) {
        if (user != null) {
            IconButton(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Выйти")
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            ) {
                if (user?.avatarUrl != null) {
                    AsyncImage(
                        model = user!!.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    DummyAvatar(Modifier.fillMaxSize())
                }
            }
            Spacer(Modifier.height(12.dp))
            if (user == null) {
                Text("Вы не авторизованы", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                OutlinedButton(onClick = { navController.navigate(AppScreen.Auth.name) }) {
                    Text("Войти")
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            } else {
                Text(user!!.name, fontWeight = FontWeight.SemiBold)
                Text(user!!.position, fontWeight = FontWeight.Light)
            }
            Spacer(Modifier.height(8.dp))
        }
    }
    HorizontalDivider()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AppBarNavItem(
                label = "Живая лента",
                icon = Icons.Default.RssFeed,
                route = AppScreen.Feed,
            )
            AppBarNavItem(
                label = "Расписание",
                icon = Icons.Default.CalendarMonth,
                route = AppScreen.Schedule,
            )
            AppBarNavItem(
                label = "Секции",
                icon = Icons.Default.SportsFootball,
                route = AppScreen.Sections,
            )
            AppBarNavItem(
                label = "Материалы",
                icon = Icons.Default.Source,
                route = AppScreen.Materials,
            )
            AppBarNavItem(
                label = "Онлайн-занятия",
                icon = Icons.Default.CastForEducation,
                route = AppScreen.Webinars,
            )
            AppBarNavItem(
                label = "Зачетная книжка",
                icon = Icons.Default.AssignmentTurnedIn,
                route = AppScreen.Marks,
            )
            AppBarNavItem(
                label = "Стипендии",
                icon = Icons.Default.CreditCard,
                route = AppScreen.Scholarships,
            )
            AppBarNavItem(
                label = "Сотрудники",
                icon = Icons.Default.Group,
                route = AppScreen.Employees,
            )
            AppBarNavItem(
                label = "Обучающиеся",
                icon = Icons.Default.Groups,
                route = AppScreen.Students,
            )
            AppBarNavItem(
                label = "Приказы",
                icon = Icons.Default.FilterFrames,
                route = AppScreen.Orders,
            )
        }

        Spacer(Modifier.weight(1f))

        HorizontalDivider()
        Box(Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
            AppBarNavItem(
                label = "Настройки",
                icon = Icons.Default.Settings,
                route = AppScreen.Settings,
            )
        }
    }
}

@Composable
fun AppBarNavItem(
    label: String,
    icon: ImageVector,
    route: AppScreen
) {
    val navController = LocalNavController.current
    NavigationDrawerItem(
        label = { Text(label) },
        icon = { Icon(icon, contentDescription = null) },
        selected = navController.currentDestination?.route == route.name,
        onClick = {
            navController.popBackStackLifecycleAware()
            navController.navigate(route.name)
        }
    )
}