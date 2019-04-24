package com.xeeshi.a24imovieproject.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductionCountry protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName("iso_3166_1")
    @Expose
    var iso31661: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null


    init {
        iso31661 = `in`.readString()
        name = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(iso31661)
        dest.writeString(name)
    }

    override fun toString(): String {
        return "ProductionCountry(iso31661=$iso31661, name=$name)"
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProductionCountry> = object : Parcelable.Creator<ProductionCountry> {
            override fun createFromParcel(`in`: Parcel): ProductionCountry {
                return ProductionCountry(`in`)
            }

            override fun newArray(size: Int): Array<ProductionCountry?> {
                return arrayOfNulls(size)
            }
        }
    }


}