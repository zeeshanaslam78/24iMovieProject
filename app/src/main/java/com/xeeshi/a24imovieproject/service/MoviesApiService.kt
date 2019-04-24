package com.xeeshi.a24imovieproject.service

import com.xeeshi.a24imovieproject.model.MovieDetails
import com.xeeshi.a24imovieproject.model.MovieIDs
import com.xeeshi.a24imovieproject.utils.CommonUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoviesApiService {
    private val movieChangeListApi: MovieChangeListApi
    private val movieDetailsApi: MovieDetailsApi

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
    private val okHttpClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()
    val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(CommonUtils.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    init {
        movieChangeListApi = retrofit.create(MovieChangeListApi::class.java)
        movieDetailsApi = retrofit.create(MovieDetailsApi::class.java)
    }

    fun getMovieChangeList(startDate: String, endDate: String) : Call<MovieIDs> =
        movieChangeListApi.getMovieChangeList(startDate, endDate)

    fun getMovieDetails(movieId : String) : Call<MovieDetails> = movieDetailsApi.getMovieDetails(movieId)

}