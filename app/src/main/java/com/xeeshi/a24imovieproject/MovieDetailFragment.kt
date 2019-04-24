package com.xeeshi.a24imovieproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.bumptech.glide.Glide
import com.xeeshi.a24imovieproject.model.MovieDetails
import com.xeeshi.a24imovieproject.utils.CommonUtils
import kotlinx.android.synthetic.main.movie_detail.view.*
import timber.log.Timber

class MovieDetailFragment : Fragment() {

    private var movieDetails: MovieDetails? = null
    private var isTab: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_MOVIE_DETAILS)) {
                movieDetails = it.getParcelable(ARG_MOVIE_DETAILS)
                Timber.i( movieDetails.toString())
                activity?.title = movieDetails?.title
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.movie_detail, container, false)
        if (null != layoutView.md_ll_parent) {
            isTab = true
        }
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.e("onViewCreated Called")

        view.md_tv_title.text = movieDetails?.title

        view.md_tv_language.visibility = View.GONE
        for(language in movieDetails?.spokenLanguages!!) {
            if (null != language.iso6391 && language.iso6391.equals(movieDetails?.originalLanguage)) {
                view.md_tv_language.text = language.name
                view.md_tv_language.visibility = View.VISIBLE
                break
            }
        }

        val strBuilder = StringBuilder()
        for(i in 0 until movieDetails?.genres?.size!! step 1) {
            Timber.e("${movieDetails?.genres?.get(i)}")
            if (i != (movieDetails?.genres?.size!! - 1)) {
                strBuilder.append(movieDetails?.genres?.get(i)?.name).append(" | ")
            } else {
                strBuilder.append(movieDetails?.genres?.get(i)?.name)
            }
        }
        view.md_tv_genre.text = strBuilder.toString()

        view.md_tv_release_date.text = movieDetails?.releaseDate
        view.md_tv_release_date.visibility = View.VISIBLE

        val duration: String = if (movieDetails?.runtime.toString() != "null") "0" else movieDetails?.runtime.toString()
        view.md_rv_duration.text = String.format(getString(R.string.mins), duration)
        view.md_rv_duration.visibility = View.VISIBLE

        val num = movieDetails?.popularity?.div(2.0)
        view.md_rating_bar.stepSize = 0.01f
        view.md_rating_bar.rating = num?.toFloat()!!
        view.md_rating_bar.visibility = View.VISIBLE

        view.md_tv_overview_heading.text = resources.getString(R.string.overview)

        if (null != movieDetails?.tagline && movieDetails?.tagline?.length!! > 0 )
            view.md_tv_tag_line.text = movieDetails?.tagline
        else view.md_tv_tag_line.visibility = View.GONE

        view.md_tv_overview.text = movieDetails?.overview

        val orientation = resources.configuration.orientation
        if (CommonUtils.isTablet(activity!!)) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                loadImageWithCenterCrop(view)
            } else {
                loadImageWithoutCrop(view)
            }
        } else {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                loadImageWithCenterCrop(view)
            } else {
                loadImageWithoutCrop(view)
            }
        }

        view.md_img_play_trailer.visibility = View.VISIBLE
        view.md_img_play_trailer.setOnClickListener {
            if (movieDetails?.videos?.results?.size!! > 0) {
                for (videoResult in movieDetails?.videos?.results!!) {
                    if (videoResult.type?.equals("Trailer")!!) {
                        extractYoutubeUrl(activity!!, CommonUtils.YOUTUBE_URL + videoResult.key)
                        break
                    } else if (videoResult.size == 720) {
                        extractYoutubeUrl(activity!!, CommonUtils.YOUTUBE_URL + videoResult.key)
                        break
                    } else if (videoResult.key != null) {
                        extractYoutubeUrl(activity!!, CommonUtils.YOUTUBE_URL + videoResult.key)
                        break
                    } // else next iteration
                }
            } else {
                Toast.makeText(activity!!, "No Video Found", Toast.LENGTH_LONG).show()
                view.md_img_play_trailer.visibility = View.GONE
            }

        }

    }

    private fun loadImageWithCenterCrop(view: View) {
        Glide.with(activity!!)
            .load(CommonUtils.BASE_URL_ORIGINAL_IMAGE + movieDetails?.posterPath)
            .error(R.drawable.image_not_found)
            .centerCrop()
            .into(view.md_img_poster)
    }

    private fun loadImageWithoutCrop(view: View) {
        Glide.with(activity!!)
            .load(CommonUtils.BASE_URL_ORIGINAL_IMAGE + movieDetails?.posterPath)
            .error(R.drawable.image_not_found)
            .into(view.md_img_poster)
    }

    private fun extractYoutubeUrl(context: Context, youtubeUrl: String) {
        @SuppressLint("StaticFieldLeak")
        val mExtractor = object : YouTubeExtractor(activity!!) {
            protected override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta) {
                if (ytFiles != null) {

                    var i = 0
                    var itag: Int
                    while (i < ytFiles.size()) {
                        itag = ytFiles.keyAt(i)
                        // ytFile represents one file with its url and meta data
                        val ytFile = ytFiles.get(itag)

                        // Just add videos in a decent format => height -1 = audio
                        if (ytFile.format.height == -1 || ytFile.format.height > 360) {
                            Timber.i(
                                "YouTubeExtractor + onExtractionComplete: videoMeta %s YtFiles %s",
                                videoMeta.toString(), ytFile.toString()
                            )
                            val intent = Intent(context, TrailerPlaybackActivity::class.java).apply {
                                putExtra(TrailerPlaybackActivity.ARG_TRAILER_URL, ytFile.url)
                            }
                            context.startActivity(intent)
                            break
                        }
                        i++
                    }
                }
            }
        }
        mExtractor.extract(youtubeUrl, true, false)
    }

    companion object {
       const val ARG_MOVIE_DETAILS = "movie_details"
   }

}
