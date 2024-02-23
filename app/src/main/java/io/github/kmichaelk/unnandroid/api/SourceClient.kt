package io.github.kmichaelk.unnandroid.api

import io.github.kmichaelk.unnandroid.models.source.SourceSemester
import io.github.kmichaelk.unnandroid.models.source.SourceSubject
import io.github.kmichaelk.unnandroid.models.source.SourceWebinar

interface SourceClient {

    suspend fun getMaterials(semester: Int, year: Int) : List<SourceSubject>
    suspend fun getWebinars(semester: Int, year: Int) : List<SourceWebinar>
    suspend fun getSemesters() : List<SourceSemester>
}