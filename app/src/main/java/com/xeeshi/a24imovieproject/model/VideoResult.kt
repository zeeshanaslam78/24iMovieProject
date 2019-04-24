package com.xeeshi.a24imovieproject.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VideoResult protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("iso_639_1")
    @Expose
    var iso6391: String? = null
    @SerializedName("iso_3166_1")
    @Expose
    var iso31661: String? = null
    @SerializedName("key")
    @Expose
    var key: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("site")
    @Expose
    var site: String? = null
    @SerializedName("size")
    @Expose
    var size: Int? = null
    @SerializedName("type")
    @Expose
    var type: String? = null


    init {
        id = `in`.readString()
        iso6391 = `in`.readString()
        iso31661 = `in`.readString()
        key = `in`.readString()
        name = `in`.readString()
        site = `in`.readString()
        size = if (`in`.readByte().toInt() == 0x00) null else `in`.readInt()
        type = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(iso6391)
        dest.writeString(iso31661)
        dest.writeString(key)
        dest.writeString(name)
        dest.writeString(site)
        if (size == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeInt(size!!)
        }
        dest.writeString(type)
    }

    override fun toString(): String {
        return "VideoResult(id=$id, iso6391=$iso6391, iso31661=$iso31661, key=$key, name=$name, site=$site, size=$size, type=$type)"
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VideoResult> = object : Parcelable.Creator<VideoResult> {
            override fun createFromParcel(`in`: Parcel): VideoResult {
                return VideoResult(`in`)
            }

            override fun newArray(size: Int): Array<VideoResult?> {
                return arrayOfNulls(size)
            }
        }
    }


}