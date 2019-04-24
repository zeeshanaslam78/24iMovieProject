package com.xeeshi.a24imovieproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("adult")
    @Expose
    var adult: Boolean? = null

    var movieDetails: MovieDetails? = null

    override fun toString(): String {
        return "Result(id=$id, adult=$adult)"
    }


}