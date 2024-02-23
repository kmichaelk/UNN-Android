package io.github.kmichaelk.unnandroid.models.journal

data class JournalSection(
    val id: Int,
    val type: String,
    val trainer: String,
    val auditorium: String,
    val timespan: String,
    val status: Status,
) {
    enum class Status { Available, NotAvailable, Booked }
}