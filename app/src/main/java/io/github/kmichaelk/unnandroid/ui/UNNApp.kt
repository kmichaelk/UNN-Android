package io.github.kmichaelk.unnandroid.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.managers.AuthManager
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
import io.github.kmichaelk.unnandroid.ui.screens.AboutScreen
import io.github.kmichaelk.unnandroid.ui.screens.StudentsSearchScreen
import io.github.kmichaelk.unnandroid.ui.screens.AuthScreen
import io.github.kmichaelk.unnandroid.ui.screens.EmployeeSearchScreen
import io.github.kmichaelk.unnandroid.ui.screens.FeedScreen
import io.github.kmichaelk.unnandroid.ui.screens.MarksScreen
import io.github.kmichaelk.unnandroid.ui.screens.MaterialsScreen
import io.github.kmichaelk.unnandroid.ui.screens.OrdersScreen
import io.github.kmichaelk.unnandroid.ui.screens.PostScreen
import io.github.kmichaelk.unnandroid.ui.screens.ScheduleScreen
import io.github.kmichaelk.unnandroid.ui.screens.ScholarshipsScreen
import io.github.kmichaelk.unnandroid.ui.screens.SectionsListScreen
import io.github.kmichaelk.unnandroid.ui.screens.SectionsScreen
import io.github.kmichaelk.unnandroid.ui.screens.UserDetailScreen
import io.github.kmichaelk.unnandroid.ui.screens.WebinarsScreen
import javax.inject.Inject

val LocalAuthState = compositionLocalOf<Boolean> { error("Auth State not set") }

@Composable
fun UNNApp(
    viewModel: UNNAppViewModel = hiltViewModel()
) {

    val navController = rememberNavController()

    val user by viewModel.user.collectAsState()
    val isAuthorized = user != null

    CompositionLocalProvider(LocalNavController provides navController) {
        CompositionLocalProvider(LocalAuthState provides isAuthorized) {
            NavHost(
                navController = navController,
                startDestination = AppScreen.Schedule.name
            ) {
                composable(route = AppScreen.Auth.name) {
                    AuthScreen(
                        onFinish = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(route = AppScreen.Feed.name) {
                    AuthGuard { FeedScreen() }
                }
                composable(route = AppScreen.Schedule.name) {
                    ScheduleScreen()
                }
                composable(route = AppScreen.Sections.name) {
                    AuthGuard { SectionsScreen() }
                }
                composable(route = AppScreen.Materials.name) {
                    AuthGuard { MaterialsScreen() }
                }
                composable(route = AppScreen.Webinars.name) {
                    AuthGuard { WebinarsScreen() }
                }
                composable(route = AppScreen.Marks.name) {
                    AuthGuard { MarksScreen() }
                }
                composable(route = AppScreen.Scholarships.name) {
                    AuthGuard { ScholarshipsScreen() }
                }
                composable(route = AppScreen.Employees.name) {
                    AuthGuard { EmployeeSearchScreen() }
                }
                composable(route = AppScreen.Students.name) {
                    AuthGuard { StudentsSearchScreen() }
                }
                composable(route = AppScreen.Orders.name) {
                    AuthGuard { OrdersScreen() }
                }

                composable(route = AppScreen.Settings.name) {
                    AboutScreen()
                }

                composable(route = AppScreen.SectionsTimetable.name) {
                    AuthGuard { SectionsListScreen() }
                }

                composable(route = AppScreen.FeedPost.name) {
                    val post: PortalFeedPost = it.arguments!!.getParcelable("post")!!
                    AuthGuard {
                        PostScreen(post = post)
                    }
                }
                composable(
                    route = "${AppScreen.User.name}/{userId}",
                    arguments = listOf(navArgument("userId") {
                        type = NavType.IntType
                    })
                ) {
                    val userId: Int = it.arguments!!.getInt("userId")
                    AuthGuard {
                        UserDetailScreen(userId = userId)
                    }
                }
            }
        }
    }
}

@HiltViewModel
class UNNAppViewModel @Inject constructor(
    authManager: AuthManager
) : ViewModel() {

    val user = authManager.user
}

@Composable
fun AuthGuard(
    content: @Composable () -> Unit
) {
    val navController = LocalNavController.current
    if (LocalAuthState.current) {
        content()
    } else {
        LaunchedEffect(Unit) {
            navController.navigate(AppScreen.Auth.name)
        }
    }
}