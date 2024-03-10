package io.github.kmichaelk.unnandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.TransferWithinAStation
import androidx.compose.material.icons.filled.TripOrigin
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.LocalImageLoader
import coil.imageLoader
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.portal.PortalDepartment
import io.github.kmichaelk.unnandroid.models.portal.PortalUserType
import io.github.kmichaelk.unnandroid.ui.AppScreen
import io.github.kmichaelk.unnandroid.ui.LocalNavController
import io.github.kmichaelk.unnandroid.ui.composables.DummyAvatar
import io.github.kmichaelk.unnandroid.ui.composables.FancyError
import io.github.kmichaelk.unnandroid.ui.composables.FancyLoading
import io.github.kmichaelk.unnandroid.ui.extensions.popBackStackLifecycleAware
import io.github.kmichaelk.unnandroid.ui.viewmodels.UserDetailScreenViewModel
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    viewModel: UserDetailScreenViewModel = hiltViewModel(),
    userId: Int,
) {
    val state by viewModel.uiState.collectAsState()

    viewModel.setBaseImageLoader(LocalContext.current.imageLoader)

    val navController = LocalNavController.current
    val uriHandler = LocalUriHandler.current

    val toolbarState = rememberCollapsingToolbarScaffoldState()

    val dateFormat = remember {
        SimpleDateFormat("d MMMM yyyy, HH:mm:ss", Locale("RU"))
    }

    LaunchedEffect(userId) {
        viewModel.id = userId
        viewModel.load()
    }

    state.data?.let {
        CollapsingToolbarScaffold(
            modifier = Modifier,
            state = toolbarState,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbarModifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer),
            toolbar = {
                CompositionLocalProvider(LocalImageLoader provides viewModel.imageLoader) {
                    Box(
                        Modifier
                            .parallax(0.5f)
                            .height(400.dp)
                            .graphicsLayer {
                                // change alpha of Image as the toolbar expands
                                alpha = toolbarState.toolbarState.progress
                            }
                    ) {
                        it.avatar.urlOriginal?.let { url ->
                            AsyncImage(
                                model = PortalService.P_URL + url,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        } ?: Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(0.64f)
                            )
                        }
                    }

                    TopAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .road(Alignment.BottomStart, Alignment.BottomStart),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                                alpha = 0.75f * toolbarState.toolbarState.progress
                            ),
                        ),
                        title = { Text(it.name) },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStackLifecycleAware()
                            }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                uriHandler.openUri("${PortalService.P_URL}/app/user/view/${it.id}")
                            }) {
                                Icon(Icons.Default.OpenInNew, contentDescription = "Открыть в браузере")
                            }
                        }
                    )
                }
            }
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                item { Spacer(Modifier.height(16.dp) )}

                it.profiles.forEach { profile ->
                    when (profile.type) {
                        PortalUserType.Student -> {
                            item { ListItem(
                                leadingContent = { Icon(Icons.Default.School, contentDescription = null) },
                                headlineContent = { Text("Учебное подразделение") },
                                supportingContent = { Text(profile.faculty!!.title) }
                            ) }
                            item { ListItem(
                                leadingContent = { Icon(Icons.Default.Directions, contentDescription = null) },
                                headlineContent = { Text("Направление подготовки") },
                                supportingContent = { Text(profile.eduDirection!!.title) }
                            ) }
                            profile.eduSpecialization?.let{
                                item { ListItem(
                                    leadingContent = { Icon(Icons.Default.Science, contentDescription = null) },
                                    headlineContent = { Text("Направленность (профиль)") },
                                    supportingContent = { Text(it.title) }
                                ) }
                            }
                            item { ListItem(
                                leadingContent = { Icon(Icons.Default.TransferWithinAStation, contentDescription = null) },
                                headlineContent = { Text("Квалификация") },
                                supportingContent = { Text(profile.eduQualification!!.title) }
                            ) }
                            item { ListItem(
                                leadingContent = { Icon(Icons.Default.Group, contentDescription = null) },
                                headlineContent = { Text("Группа") },
                                supportingContent = { Text(profile.eduGroup!!.title) }
                            ) }
                            item { ListItem(
                                leadingContent = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                                headlineContent = { Text("Год поступления") },
                                supportingContent = { Text(profile.eduYear.toString()) }
                            ) }
                            item { ListItem(
                                leadingContent = { Icon(Icons.Default.HistoryEdu, contentDescription = null) },
                                headlineContent = { Text("Курс") },
                                supportingContent = { Text(profile.eduCourse.toString()) }
                            ) }
                            item { ListItem(
                                leadingContent = { Icon(Icons.Default.DirectionsWalk, contentDescription = null) },
                                headlineContent = { Text("Статус обучения") },
                                supportingContent = { Text(profile.eduStatus!!) }
                            ) }
                            item { ListItem(
                                leadingContent = { Icon(Icons.Default.RemoveRedEye, contentDescription = null) },
                                headlineContent = { Text("Форма обучения") },
                                supportingContent = { Text(profile.eduForm!!) }
                            ) }
                        }
                        PortalUserType.Employee -> {
                            item { ListItem(
                                leadingContent = { Icon(Icons.Default.Circle, contentDescription = null) },
                                headlineContent = { Text("Вид работы") },
                                supportingContent = { Text(profile.jobType!!) }
                            ) }
                            item { ListItem(
                                leadingContent = { Icon(Icons.Default.TripOrigin, contentDescription = null) },
                                headlineContent = { Text("Подразделение") },
                                supportingContent = {
                                    Column(Modifier.fillMaxWidth()) {
                                        var pDepartment: PortalDepartment? = profile.department!!
                                        while (pDepartment != null) {
                                            Text(pDepartment.title)
                                            pDepartment = pDepartment.child
                                        }
                                    }
                                }
                            ) }
                            item { ListItem(
                                leadingContent = { Icon(Icons.Default.Work, contentDescription = null) },
                                headlineContent = { Text("Должность") },
                                supportingContent = { Text(profile.jobTitle!!) }
                            ) }
                            profile.manager?.let { manager ->
                                item { ListItem(
                                    leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
                                    headlineContent = { Text("Руководитель") },
                                    supportingContent = { Text(manager.name) },
                                    modifier = Modifier.clickable {
                                        navController.navigate("${AppScreen.User.name}/${manager.id}")
                                    }
                                ) }
                            }
                        }
                    }
                    item { HorizontalDivider() }
                }

                item { Spacer(Modifier.height(8.dp) )}
                item {
                    it.profiles.mapNotNull { profile -> profile.workAddress }.forEach { addr ->
                        ListItem(
                            leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                            headlineContent = { Text("Адрес рабочего места") },
                            supportingContent = { Text(addr) }
                        )
                    }
                    ListItem(
                        leadingContent = { Icon(Icons.Default.AccessTime, contentDescription = null) },
                        headlineContent = { Text("Последняя активность") },
                        supportingContent = { Text(it.lastActivity?.let { lastActivity ->
                            dateFormat.format(lastActivity) } ?: "Нет данных")
                        }
                    )
                }

                item { Spacer(Modifier.height(48.dp) )}
            }
        }
    } ?: Scaffold { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()) {
            (state.error?.let {
                FancyError(error = it, onRetry = { viewModel.load() })
            } ?: FancyLoading())
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 16.dp),
                onClick = {
                    navController.popBackStackLifecycleAware()
                }
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }
        }
    }
}