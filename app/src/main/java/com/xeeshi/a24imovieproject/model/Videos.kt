package com.xeeshi.a24imovieproject.model

import java.util.ArrayList

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Videos protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName("results")
    @Expose
    var results: List<VideoResult>? = null


    init {
        if (`in`.readByte().toInt() == 0x01) {
            results = ArrayList()
            `in`.readList(results, VideoResult::class.java.classLoader)
        } else {
            results = null
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        if (results == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeList(results)
        }
    }

    override fun toString(): String {
        return "Videos(results=$results)"
    }


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Videos> = object : Parcelable.Creator<Videos> {
            override fun createFromParcel(`in`: Parcel): Videos {
                return Videos(`in`)
            }

            override fun newArray(size: Int): Array<Videos?> {
                return arrayOfNulls(size)
            }
        }
    }
}