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
    val author: PortalFeedUser,
    val datetime: String,
    val html: String,
    val attachmentsUrls: List<String>,
    val commentsCount: Int,
    val entityXmlId: String,
    val views: Int,
    val url: String,
    val receivers: List<Receiver>,
    val files: List<PortalFeedAttachedFile>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(PortalFeedUser::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.createTypedArrayList(Receiver)!!,
        parcel.createTypedArrayList(PortalFeedAttachedFile)!!
    ) {
    }

    data class Receiver(
        val id: Int,
        val name: String,
        val type: String,
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeString(name)
            parcel.writeString(type)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Receiver> {
            override fun createFromParcel(parcel: Parcel): Receiver {
                return Receiver(parcel)
            }

            override fun newArray(size: Int): Array<Receiver?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeParcelable(author, flags)
        parcel.writeString(datetime)
        parcel.writeString(html)
        parcel.writeStringList(attachmentsUrls)
        parcel.writeInt(commentsCount)
        parcel.writeString(entityXmlId)
        parcel.writeInt(views)
        parcel.writeString(url)
        parcel.writeTypedList(receivers)
        parcel.writeTypedList(files)
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
