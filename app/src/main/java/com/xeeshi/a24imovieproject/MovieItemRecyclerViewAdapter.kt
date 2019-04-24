package com.xeeshi.a24imovieproject

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.xeeshi.a24imovieproject.model.ErrorResponse
import com.xeeshi.a24imovieproject.model.MovieDetails
import com.xeeshi.a24imovieproject.model.MovieIDs
import com.xeeshi.a24imovieproject.model.Result
import com.xeeshi.a24imovieproject.service.ApiError
import com.xeeshi.a24imovieproject.service.MoviesApiService
import com.xeeshi.a24imovieproject.utils.CommonUtils
import kotlinx.android.synthetic.main.movie_list_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MovieItemRecyclerViewAdapter(
    private val parentActivity: MovieListActivity,
    moviesIds: MovieIDs
) :
    RecyclerView.Adapter<MovieItemRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener
    private val moviesIdResults: List<Result>? = moviesIds.results

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Result
            val intent = Intent(v.context, MovieDetailActivity::class.java).apply {
                putExtra(MovieDetailFragment.ARG_MOVIE_DETAILS, item.movieDetails)
            }
            v.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = moviesIdResults?.get(position)

        holder.movieTitle.text = ""
        holder.movieTitle.visibility = View.GONE
        holder.moviePoster.setImageResource(0)

        Timber.e(" %s", item?.id)

        //if (null == item?.movieDetails) {
            Timber.e("ID %s Null", item?.id)
            MoviesApiService.getMovieDetails(item?.id.toString()).enqueue(object : Callback<MovieDetails> {
                override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                    response.let { responseMovieDetail ->
                        if (responseMovieDetail.isSuccessful) {
                            val body = responseMovieDetail.body()
                            body?.let {movieDetails ->
                                item?.movieDetails = movieDetails
                                updateItemView(item, holder)
                            }
                        } else {
                            val errorResponse: ErrorResponse = ApiError.parseError(response)
                            errorResponse.let {
                                holder.moviePoster.setImageResource(R.drawable.image_not_found)
                            }
                        }
                    }
                }
            })

        /*} else {
            Log.e("TitleTag", "id " + item?.id + " Not Null")
            //val movieDetails = holder.movieTitle.tag as MovieDetails
            //updateItemView(item, holder)
            //https://image.tmdb.org/t/p/original/lPwZDX3V6f77zt6S2mx1BLVZvRQ.jpg
            //https://image.tmdb.org/t/p/w500/qNRtZh6jbSrzLqj4BAEixeTAvMM.jpg
        }*/


        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }

    }

    private fun updateItemView(
        moviesIdsResult: Result?,
        holder: ViewHolder
    ) {
        holder.movieTitle.text = String.format("%s", moviesIdsResult?.movieDetails?.title)
        holder.movieTitle.visibility = View.VISIBLE
        parentActivity.let {
            if (moviesIdsResult?.movieDetails?.posterPath != null
                && !moviesIdsResult.movieDetails?.posterPath.equals("null")) {
                Timber.e( "PosterPath %s", moviesIdsResult.movieDetails?.posterPath)
                Glide.with(it)
                    .load(CommonUtils.BASE_URL_W500_IMAGE + moviesIdsResult.movieDetails?.posterPath)
                    .error(R.drawable.image_not_found)
                    .into(holder.moviePoster)
            } else {
                holder.moviePoster.setImageResource(R.drawable.image_not_found)
            }
        }
    }

    override fun getItemCount() = moviesIdResults?.size!!
    /*override fun getItemCount() : Int {
        moviesIdResults?.let { it -> return it.size }
        return 0
    }*/

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moviePoster: ImageView = view.movie_poster
        val movieTitle: TextView = view.movie_title
    }
}