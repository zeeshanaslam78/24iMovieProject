package com.xeeshi.a24imovieproject.service

import com.xeeshi.a24imovieproject.model.MovieIDs
import com.xeeshi.a24imovieproject.utils.CommonUtils
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieChangeListApi {

    @GET("changes?api_key=" + CommonUtils.API_KEY + "&page=1")
    fun getMovieChangeList(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String): Call<MovieIDs>

}