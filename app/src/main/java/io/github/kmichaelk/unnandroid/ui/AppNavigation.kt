package io.github.kmichaelk.unnandroid.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

val LocalNavController = compositionLocalOf<NavController> { error("No NavController found") }

enum class AppScreen {
    EntryPoint,

    Auth,

    Feed,
    Schedule,
    Sections,
    Materials,
    Webinars,
    Marks,
    Scholarships,
    Employees,
    Students,
    Orders,

    Settings,

    SectionsTimetable,

    FeedPost,
    User,
}