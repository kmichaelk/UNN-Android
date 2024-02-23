package io.github.kmichaelk.unnandroid.converters.schedule

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import io.github.kmichaelk.unnandroid.api.service.ScheduleService
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleScope
import retrofit2.Converter
import java.text.SimpleDateFormat
import java.util.Date

class ScheduleQueryConverter<T> : Converter<T, String> {

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat(ScheduleService.DATE_FORMAT)

    override fun convert(value: T): String? {
        if (value is Date) {
            return dateFormat.format(value)
        }
        if (value is ScheduleScope) {
            return value.declaringJavaClass.getField(value.name)
                .getAnnotation(SerializedName::class.java)?.value ?: value.name
        }
        throw AssertionError()
    }
}