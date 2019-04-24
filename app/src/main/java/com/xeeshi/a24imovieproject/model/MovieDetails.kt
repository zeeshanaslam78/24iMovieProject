package com.xeeshi.a24imovieproject.model

import java.util.ArrayList

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieDetails protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName("adult")
    @Expose
    var adult: Boolean? = null
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null
    @SerializedName("belongs_to_collection")
    @Expose
    var belongsToCollection: Any? = null
    @SerializedName("budget")
    @Expose
    var budget: Int? = null
    @SerializedName("genres")
    @Expose
    var genres: List<Genre>? = null
    @SerializedName("homepage")
    @Expose
    var homepage: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("imdb_id")
    @Expose
    var imdbId: String? = null
    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null
    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null
    @SerializedName("overview")
    @Expose
    var overview: String? = null
    @SerializedName("popularity")
    @Expose
    var popularity: Double? = null
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null
    @SerializedName("production_companies")
    @Expose
    var productionCompanies: List<ProductionCompany>? = null
    @SerializedName("production_countries")
    @Expose
    var productionCountries: List<ProductionCountry>? = null
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null
    @SerializedName("revenue")
    @Expose
    var revenue: Int? = null
    @SerializedName("runtime")
    @Expose
    var runtime: Int? = null
    @SerializedName("spoken_languages")
    @Expose
    var spokenLanguages: List<SpokenLanguage>? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("tagline")
    @Expose
    var tagline: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("video")
    @Expose
    var video: Boolean? = null
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null
    @SerializedName("vote_count")
    @Expose
    var voteCount: Int? = null
    @SerializedName("videos")
    @Expose
    var videos: Videos? = null


    init {
        val adultVal = `in`.readByte()
        adult = if (adultVal.toInt() == 0x02) null else adultVal.toInt() != 0x00
        backdropPath = `in`.readString()
        belongsToCollection = `in`.readValue(Any::class.java.classLoader) as? Any
        budget = if (`in`.readByte().toInt() == 0x00) null else `in`.readInt()
        if (`in`.readByte().toInt() == 0x01) {
            genres = ArrayList()
            `in`.readList(genres, Genre::class.java.classLoader)
        } else {
            genres = null
        }
        homepage = `in`.readString()
        id = if (`in`.readByte().toInt() == 0x00) null else `in`.readInt()
        imdbId = `in`.readString()
        originalLanguage = `in`.readString()
        originalTitle = `in`.readString()
        overview = `in`.readString()
        popularity = if (`in`.readByte().toInt() == 0x00) null else `in`.readDouble()
        posterPath = `in`.readString()
        if (`in`.readByte().toInt() == 0x01) {
            productionCompanies = ArrayList()
            `in`.readList(productionCompanies, ProductionCompany::class.java.classLoader)
        } else {
            productionCompanies = null
        }
        if (`in`.readByte().toInt() == 0x01) {
            productionCountries = ArrayList()
            `in`.readList(productionCountries, ProductionCountry::class.java.classLoader)
        } else {
            productionCountries = null
        }
        releaseDate = `in`.readString()
        revenue = if (`in`.readByte().toInt() == 0x00) null else `in`.readInt()
        runtime = if (`in`.readByte().toInt() == 0x00) null else `in`.readInt()
        if (`in`.readByte().toInt() == 0x01) {
            spokenLanguages = ArrayList()
            `in`.readList(spokenLanguages, SpokenLanguage::class.java.classLoader)
        } else {
            spokenLanguages = null
        }
        status = `in`.readString()
        tagline = `in`.readString()
        title = `in`.readString()
        val videoVal = `in`.readByte()
        video = if (videoVal.toInt() == 0x02) null else videoVal.toInt() != 0x00
        voteAverage = if (`in`.readByte().toInt() == 0x00) null else `in`.readDouble()
        voteCount = if (`in`.readByte().toInt() == 0x00) null else `in`.readInt()
        videos = `in`.readValue(Videos::class.java.classLoader) as Videos
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        if (adult == null) {
            dest.writeByte(0x02.toByte())
        } else {
            dest.writeByte((if (adult as Boolean) 0x01 else 0x00).toByte())
        }
        dest.writeString(backdropPath)
        dest.writeValue(belongsToCollection)
        if (budget == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeInt(budget!!)
        }
        if (genres == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeList(genres)
        }
        dest.writeString(homepage)
        if (id == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeInt(id!!)
        }
        dest.writeString(imdbId)
        dest.writeString(originalLanguage)
        dest.writeString(originalTitle)
        dest.writeString(overview)
        if (popularity == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeDouble(popularity!!)
        }
        dest.writeString(posterPath)
        if (productionCompanies == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeList(productionCompanies)
        }
        if (productionCountries == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeList(productionCountries)
        }
        dest.writeString(releaseDate)
        if (revenue == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeInt(revenue!!)
        }
        if (runtime == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeInt(runtime!!)
        }
        if (spokenLanguages == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeList(spokenLanguages)
        }
        dest.writeString(status)
        dest.writeString(tagline)
        dest.writeString(title)
        if (video == null) {
            dest.writeByte(0x02.toByte())
        } else {
            dest.writeByte((if (video as Boolean) 0x01 else 0x00).toByte())
        }
        if (voteAverage == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeDouble(voteAverage!!)
        }
        if (voteCount == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeInt(voteCount!!)
        }
        dest.writeValue(videos)
    }

    override fun toString(): String {
        return "MovieDetails(adult=$adult, backdropPath=$backdropPath, belongsToCollection=$belongsToCollection, budget=$budget, genres=$genres, homepage=$homepage, id=$id, imdbId=$imdbId, originalLanguage=$originalLanguage, originalTitle=$originalTitle, overview=$overview, popularity=$popularity, posterPath=$posterPath, productionCompanies=$productionCompanies, productionCountries=$productionCountries, releaseDate=$releaseDate, revenue=$revenue, runtime=$runtime, spokenLanguages=$spokenLanguages, status=$status, tagline=$tagline, title=$title, video=$video, voteAverage=$voteAverage, voteCount=$voteCount, videos=$videos)"
    }


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MovieDetails> = object : Parcelable.Creator<MovieDetails> {
            override fun createFromParcel(`in`: Parcel): MovieDetails {
                return MovieDetails(`in`)
            }

            override fun newArray(size: Int): Array<MovieDetails?> {
                return arrayOfNulls(size)
            }
        }
    }


}