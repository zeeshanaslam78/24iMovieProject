package com.xeeshi.a24imovieproject.service

import com.xeeshi.a24imovieproject.model.MovieDetails
import com.xeeshi.a24imovieproject.utils.CommonUtils
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailsApi {

    @GET("{movie_id}?api_key=" + CommonUtils.API_KEY + "&language=en-US&append_to_response=videos")
    fun getMovieDetails(@Path("movie_id")  movieID : String) : Call<MovieDetails>

}