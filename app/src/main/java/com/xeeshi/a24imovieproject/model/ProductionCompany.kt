package com.xeeshi.a24imovieproject.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductionCompany protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("logo_path")
    @Expose
    var logoPath: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("origin_country")
    @Expose
    var originCountry: String? = null


    init {
        id = if (`in`.readByte().toInt() == 0x00) null else `in`.readInt()
        logoPath = `in`.readString()
        name = `in`.readString()
        originCountry = `in`.readString()
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
        dest.writeString(logoPath)
        dest.writeString(name)
        dest.writeString(originCountry)
    }

    override fun toString(): String {
        return "ProductionCompany(id=$id, logoPath=$logoPath, name=$name, originCountry=$originCountry)"
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProductionCompany> = object : Parcelable.Creator<ProductionCompany> {
            override fun createFromParcel(`in`: Parcel): ProductionCompany {
                return ProductionCompany(`in`)
            }

            override fun newArray(size: Int): Array<ProductionCompany?> {
                return arrayOfNulls(size)
            }
        }
    }


}