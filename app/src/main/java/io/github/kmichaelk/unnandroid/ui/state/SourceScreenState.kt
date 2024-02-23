package io.github.kmichaelk.unnandroid.ui.state

import io.github.kmichaelk.unnandroid.models.source.SourceSemester

data class SourceScreenState(
    val semester: SourceSemester? = null,
    val semesters: List<SourceSemester>? = null
)
