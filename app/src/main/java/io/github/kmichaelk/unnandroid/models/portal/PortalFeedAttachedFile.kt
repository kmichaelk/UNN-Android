package io.github.kmichaelk.unnandroid.models.portal

import android.os.Parcel
import android.os.Parcelable

data class PortalFeedAttachedFile(
    val title: String,
    val size: String,
    val url: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(size)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PortalFeedAttachedFile> {
        override fun createFromParcel(parcel: Parcel): PortalFeedAttachedFile {
            return PortalFeedAttachedFile(parcel)
        }

        override fun newArray(size: Int): Array<PortalFeedAttachedFile?> {
            return arrayOfNulls(size)
        }
    }

}
