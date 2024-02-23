package io.github.kmichaelk.unnandroid.ui.state

import io.github.kmichaelk.unnandroid.models.journal.JournalSectionInfo

data class SectionInfoBottomSheetState(
    val data: JournalSectionInfo? = null,

    val isLoading: Boolean = false,
    val error: UiError? = null,

    val isSubmitting: Boolean = false,
    val submitError: String? = null,

    val isSuccessful: Boolean? = null
)
