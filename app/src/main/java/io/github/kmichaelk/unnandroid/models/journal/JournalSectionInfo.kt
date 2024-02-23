package io.github.kmichaelk.unnandroid.models.journal

data class JournalSectionInfo(
    var id: String = "",
    val type: String,
    val time: String,
    val trainer: String,
    val trainerUrl: String,
    val auditorium: String,
    val groups: String,
    val faculty: String,
    val healthGroups: String,
    val auditoriumCapacity: String,
    val trainerMax: String,
    val attendDay: String,
    val attendTime: String,
    val status: Status,
    val denialNotice: String,
) {
    enum class Status { Available, NotAvailable, Booked }
}
