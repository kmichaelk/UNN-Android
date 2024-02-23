package io.github.kmichaelk.unnandroid.converters.journal

import android.annotation.SuppressLint
import io.github.kmichaelk.unnandroid.api.service.JournalService
import retrofit2.Converter
import java.text.SimpleDateFormat
import java.util.Date

class JournalDateConverter<T> : Converter<T, String> {

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat(JournalService.DATE_FORMAT)

    override fun convert(value: T): String? {
        if (value is Date) {
            return dateFormat.format(value)
        }
        throw AssertionError()
    }
}