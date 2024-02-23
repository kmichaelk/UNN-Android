package io.github.kmichaelk.unnandroid.models.source

data class SourceSemester(
    val yearBegin: Int,
    val yearEnd: Int,
    val yearId: Int,
    val ord: Int,
    val season: Season,
) {
    enum class Season { Spring, Fall }
}
