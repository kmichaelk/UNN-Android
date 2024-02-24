package io.github.kmichaelk.unnandroid.ui.viewmodels

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.ScheduleClient
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleDateRange
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleEntity
import io.github.kmichaelk.unnandroid.ui.state.ScheduleScreenState
import io.github.kmichaelk.unnandroid.ui.state.UiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ScheduleScreenViewModel @Inject constructor(
    private val scheduleClient: ScheduleClient,
    @Named("schedule") private val prefs: SharedPreferences,
) : ViewModel() {

    private companion object {
        const val PREF_BOOKMARKS = "bookmarks"
    }

    private val gson = Gson()

    private val _bookmarks = MutableStateFlow(
        gson.fromJson(prefs.getString(PREF_BOOKMARKS, "[]"), Array<ScheduleEntity>::class.java)
            .toList()
    )
    val bookmarks = _bookmarks.asStateFlow()

    private val _uiState = MutableStateFlow(ScheduleScreenState(
        entity = if (_bookmarks.value.isNotEmpty()) _bookmarks.value[0] else null,
        range = ScheduleDateRange.thisWeek()
    ))
    val uiState = _uiState.asStateFlow()

    fun load(): Job? {
        val entity = _uiState.value.entity ?: return null

        _uiState.update {
            it.copy(
                error = null,
            )
        }
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                val schedule = scheduleClient.loadSchedule(entity, _uiState.value.range) {
                    if (it.lecturer != null) {
                        it.lecturer = it.lecturer!!.replace("!", "")
                    } else {
                        it.lecturer = "Вакансия"
                    }
                    if (it.lecturerRank != null) {
                        it.lecturerRank = it.lecturerRank!!
                            .replace("!", "")
                            .lowercase()
                    }
                    if (it.stream == null) {
                        it.stream = "Группа по умолчанию"
                    }
                    it
                }
                _uiState.update {
                    it.copy(
                        schedule = schedule
                    )
                }
            } catch (ex: Exception) {
                Timber.e("Failed to load schedule", ex)
                _uiState.update {
                    it.copy(
                        error = UiError.from(ex),
                    )
                }
            }
        }
    }

    fun setEntity(entity: ScheduleEntity) {
        if (bookmarks.value.isEmpty()) {
            addBookmark(entity)
        }
        _uiState.update {
            it.copy(
                entity = entity,
                schedule = null,
            )
        }
    }

    fun setRange(range: ScheduleDateRange): Boolean {
        if (_uiState.value.range == range) {
            return false
        }
        _uiState.update {
            it.copy(
                range = range
            )
        }
        return true
    }

    fun setRange(begin: Date, end: Date) = setRange(ScheduleDateRange(begin, end))

    fun isBookmarked(entity: ScheduleEntity) =
        _bookmarks.value.contains(entity)

    fun addBookmark(entity: ScheduleEntity) {
        assert(!isBookmarked(entity))
        _bookmarks.update {
            it.toMutableList().apply { add(entity) }
        }
        saveBookmarks()
    }

    fun removeBookmark(entity: ScheduleEntity) {
        assert(isBookmarked(entity))
        _bookmarks.update {
            it.toMutableList().apply { remove(entity) }
        }
        saveBookmarks()
    }

    fun setBookmarkAsDefault(entity: ScheduleEntity) {
        assert(isBookmarked(entity))
        _bookmarks.update {
            it.toMutableList().apply {
                remove(entity)
                add(0, entity)
            }
        }
        saveBookmarks()
    }

    fun isDefaultBookmark(entity: ScheduleEntity) =
        _bookmarks.value.indexOf(entity) == 0

    private fun saveBookmarks() = prefs.edit {
        putString(PREF_BOOKMARKS, gson.toJson(_bookmarks.value))
    }
}