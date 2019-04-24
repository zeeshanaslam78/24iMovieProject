package com.xeeshi.a24imovieproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieIDs {

    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
    @SerializedName("page")
    @Expose
    var page: Int? = null
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null
    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null

    override fun toString(): String {
        return "MovieIDs(results=$results, page=$page, totalPages=$totalPages, totalResults=$totalResults)"
    }


}
