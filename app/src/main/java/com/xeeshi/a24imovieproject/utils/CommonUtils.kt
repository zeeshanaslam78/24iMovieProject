package com.xeeshi.a24imovieproject.utils

import android.content.Context
import android.net.ConnectivityManager

object CommonUtils {

    const val API_KEY = "03df54d185f5b222652cda2efe0c08b5"  // the Movie db api key
    const val BASE_URL = "https://api.themoviedb.org/3/movie/"
    const val BASE_URL_ORIGINAL_IMAGE = "https://image.tmdb.org/t/p/original"
    const val BASE_URL_W500_IMAGE = "https://image.tmdb.org/t/p/w500"
    const val YOUTUBE_URL = "https://www.youtube.com/watch?v="



    fun isOnline(context: Context) : Boolean {
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    fun isTablet(context: Context): Boolean {
        return try {
            // Compute screen size
            val dm = context.resources.displayMetrics
            val screenWidth = dm.widthPixels / dm.xdpi
            val screenHeight = dm.heightPixels / dm.ydpi
            val size = Math.sqrt(Math.pow(screenWidth.toDouble(), 2.0) + Math.pow(screenHeight.toDouble(), 2.0))
            // Tablet devices have a screen size greater than 6 inches
            size >= 6
        } catch (t: Throwable) {
            false
        }
    }

}
