package io.github.kmichaelk.unnandroid.models.portal

import android.os.Parcel
import android.os.Parcelable

data class PortalFeedPost(
    val id: Int,
    val author: PortalFeedUser,
    val datetime: String,
    val html: String,
    val attachmentUrl: String?,
    val commentsCount: Int,
    val entityXmlId: String,
    val views: Int,
    val url: String,
    val receivers: List<Receiver>,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(PortalFeedUser::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.createTypedArrayList(Receiver)!!
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
        parcel.writeString(attachmentUrl)
        parcel.writeInt(commentsCount)
        parcel.writeString(entityXmlId)
        parcel.writeInt(views)
        parcel.writeString(url)
        parcel.writeTypedList(receivers)
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
