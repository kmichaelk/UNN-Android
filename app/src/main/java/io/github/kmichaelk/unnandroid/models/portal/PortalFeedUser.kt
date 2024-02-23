package io.github.kmichaelk.unnandroid.models.portal

import android.os.Parcel
import android.os.Parcelable

data class PortalFeedUser(
    val bxId: Int,
    val name: String,
    val avatarUrl: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bxId)
        parcel.writeString(name)
        parcel.writeString(avatarUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PortalFeedUser> {
        override fun createFromParcel(parcel: Parcel): PortalFeedUser {
            return PortalFeedUser(parcel)
        }

        override fun newArray(size: Int): Array<PortalFeedUser?> {
            return arrayOfNulls(size)
        }
    }

}
