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

package io.github.kmichaelk.unnandroid.models.portal

import android.os.Parcel
import android.os.Parcelable

data class PortalFeedPost(
    val id: Int,
    val entityXmlId: String,
    val author: PortalFeedUser,
    val datetime: String,

    val html: String,

    val attachments: List<String>,
    val files: List<PortalFeedAttachedFile>,

    val receivers: List<PortalFeedPostReceiver>,
    val commentsCount: Int,
    val views: Int,

    val url: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readParcelable(PortalFeedUser::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.createTypedArrayList(PortalFeedAttachedFile)!!,
        parcel.createTypedArrayList(PortalFeedPostReceiver)!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(entityXmlId)
        parcel.writeParcelable(author, flags)
        parcel.writeString(datetime)
        parcel.writeString(html)
        parcel.writeStringList(attachments)
        parcel.writeTypedList(files)
        parcel.writeTypedList(receivers)
        parcel.writeInt(commentsCount)
        parcel.writeInt(views)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PortalFeedPost> {
        override fun createFromParcel(parcel: Parcel): PortalFeedPost {
            return PortalFeedPost(parcel)
        }

        override fun newArray(size: Int): Array<PortalFeedPost?> {
            return arrayOfNulls(size)
        }
    }

}
