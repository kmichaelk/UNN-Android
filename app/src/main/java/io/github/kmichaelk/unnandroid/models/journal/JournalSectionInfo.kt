/*
 * This file is part of UNN-Android.
 *
 * UNN-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UNN-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UNN-Android.  If not, see <https://www.gnu.org/licenses/>.
 */

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
