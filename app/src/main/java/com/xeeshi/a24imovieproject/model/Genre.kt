package com.xeeshi.a24imovieproject.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Genre protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null


    init {
        id = if (`in`.readByte().toInt() == 0x00) null else `in`.readInt()
        name = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        if (id == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeInt(id!!)
        }
        dest.writeString(name)
    }

    override fun toString(): String {
        return "Genre(id=$id, name=$name)"
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Genre> = object : Parcelable.Creator<Genre> {
            override fun createFromParcel(`in`: Parcel): Genre {
                return Genre(`in`)
            }

            override fun newArray(size: Int): Array<Genre?> {
                return arrayOfNulls(size)
            }
        }
    }


}