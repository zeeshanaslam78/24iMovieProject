package com.xeeshi.a24imovieproject.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SpokenLanguage protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName("iso_639_1")
    @Expose
    var iso6391: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null


    init {
        iso6391 = `in`.readString()
        name = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(iso6391)
        dest.writeString(name)
    }

    override fun toString(): String {
        return "SpokenLanguage(iso6391=$iso6391, name=$name)"
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SpokenLanguage> = object : Parcelable.Creator<SpokenLanguage> {
            override fun createFromParcel(`in`: Parcel): SpokenLanguage {
                return SpokenLanguage(`in`)
            }

            override fun newArray(size: Int): Array<SpokenLanguage?> {
                return arrayOfNulls(size)
            }
        }
    }



}