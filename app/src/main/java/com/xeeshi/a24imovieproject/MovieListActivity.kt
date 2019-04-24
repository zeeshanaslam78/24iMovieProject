package com.xeeshi.a24imovieproject

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.Toast
import com.xeeshi.a24imovieproject.model.ErrorResponse
import com.xeeshi.a24imovieproject.model.MovieIDs
import com.xeeshi.a24imovieproject.service.ApiError
import com.xeeshi.a24imovieproject.service.MoviesApiService
import com.xeeshi.a24imovieproject.utils.CommonUtils
import com.xeeshi.a24imovieproject.utils.GridAutofitLayoutManager
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.android.synthetic.main.movie_list.*
import kotlinx.android.synthetic.main.pick_date_range.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MovieListActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val formattedDate = simpleDateFormat.format(cal.time)
        Timber.i(formattedDate)

        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        // end_date > start_date
        if (pdr_tv_start_date != null && pdr_tv_end_date != null) {
            if (isStartDate) {
                if (pdr_tv_end_date.text.isNotEmpty()) {
                    val endDate = getCalenderInstanceOfGivenDate(pdr_tv_end_date.text.toString())

                    endDate.set(Calendar.HOUR_OF_DAY, 0)
                    endDate.set(Calendar.MINUTE, 0)
                    endDate.set(Calendar.SECOND, 0)
                    endDate.set(Calendar.MILLISECOND, 0)

                    if (endDate.time.after(cal.time))
                        pdr_tv_start_date.text = formattedDate
                     else {
                        pdr_tv_start_date.text = ""
                        Toast.makeText(this, "Can not choose after end date or same date", Toast.LENGTH_LONG).show()
                    }

                } else pdr_tv_start_date.text = formattedDate
            } else {
                if (pdr_tv_start_date.text.isNotEmpty()) {
                    val startDate = getCalenderInstanceOfGivenDate(pdr_tv_start_date.text.toString())

                    startDate.set(Calendar.HOUR_OF_DAY, 0)
                    startDate.set(Calendar.MINUTE, 0)
                    startDate.set(Calendar.SECOND, 0)
                    startDate.set(Calendar.MILLISECOND, 0)

                    if (cal.time.after(startDate.time)) {
                        pdr_tv_end_date.text = formattedDate
                    } else {
                        pdr_tv_end_date.text = ""
                        Toast.makeText(this, "Can not choose before start date or same date", Toast.LENGTH_LONG).show()
                    }
                } else pdr_tv_end_date.text = formattedDate
            }

        }


    }

    private fun getCalenderInstanceOfGivenDate(strDate: String) : Calendar {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val date: Date = simpleDateFormat.parse(strDate)
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        return cal
    }

    private var isTab: Boolean = false
    private var isStartDate: Boolean = false
    private lateinit var slideUpAnimation : Animation
    private lateinit var slideDownAnimation : Animation
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        setSupportActionBar(toolbar)
        toolbar.title = title

        slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up_animation)
        slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down_animation)

        slideUpAnimation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) { }

            override fun onAnimationEnd(p0: Animation?) {
                pdr_cl_parent.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) { }
        })
        slideDownAnimation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) { }

            override fun onAnimationEnd(p0: Animation?) {
                pdr_cl_parent.visibility = View.VISIBLE
            }

            override fun onAnimationStart(p0: Animation?) {
                pdr_cl_parent.visibility = View.VISIBLE
            }
        })


        if (list_tab_view != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTab = true
        }

        pdr_cl_parent.visibility = View.VISIBLE
        pdr_cl_parent.startAnimation(slideDownAnimation)

        pdr_tv_start_date.setOnClickListener {
            isStartDate = true
            showDatePicketDialog()
        }

        pdr_tv_end_date.setOnClickListener {
            isStartDate = false
            showDatePicketDialog()
        }

        pdr_btn_ok.setOnClickListener {

            if (pdr_tv_start_date.text.isNotEmpty() && pdr_tv_end_date.text.isNotEmpty()) {
                if (null != snackBar && snackBar?.isShown!!)
                    snackBar?.dismiss()
                pdr_btn_ok.isEnabled = false
                ml_progressbar.visibility = View.VISIBLE
                getMoviesIDs(pdr_tv_start_date.text.toString(), pdr_tv_end_date.text.toString())
            }
            else
                Toast.makeText(this, "Please choose start date and end date to get movies", Toast.LENGTH_LONG).show()
        }
    }

    private fun showDatePicketDialog() {
        val cal = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this, this,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun getMoviesIDs(startDate: String, endDate: String) {
        if (CommonUtils.isOnline(this)) {

            MoviesApiService.getMovieChangeList(startDate, endDate).enqueue(object : Callback<MovieIDs> {

                override fun onFailure(call: Call<MovieIDs>, t: Throwable) {
                    t.printStackTrace()
                    pdr_btn_ok.isEnabled = true
                    ml_progressbar.visibility = View.GONE
                    if (null != snackBar && snackBar?.isShown!!)
                        snackBar?.dismiss()
                    snackBar = Snackbar.make(frameLayout, "Try Again", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok") {
                            pdr_btn_ok.isEnabled = false
                            ml_progressbar.visibility = View.VISIBLE
                            getMoviesIDs(startDate, endDate)
                        }
                    snackBar!!.show()
                }

                override fun onResponse(call: Call<MovieIDs>, response: Response<MovieIDs>) {
                    response.let { responseMoviesIDs ->
                        pdr_btn_ok.isEnabled = true
                        ml_progressbar.visibility = View.GONE

                        if (responseMoviesIDs.isSuccessful) {
                            val body = responseMoviesIDs.body()
                            body?.let { moviesIds ->
                                Timber.i("MoviesIds %s", moviesIds.toString())
                                Timber.i("MoviesIds Results Size %s", moviesIds.results?.size)
                                moviesIds.results = moviesIds.results?.filter {
                                    it.adult == false
                                }
                                Timber.i("MoviesIds Results Size After filter %s", moviesIds.results?.size)


                                pdr_cl_parent.visibility = View.VISIBLE
                                pdr_cl_parent.startAnimation(slideUpAnimation)

                                setupRecyclerView(item_list, moviesIds)


                            }
                        } else {
                            val errorResponse: ErrorResponse = ApiError.parseError(response)
                            errorResponse.let {
                                Toast.makeText(
                                    this@MovieListActivity,
                                    errorResponse.statusMessage, Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            })
        } else {
            pdr_btn_ok.isEnabled = true
            ml_progressbar.visibility = View.GONE

            if (null != snackBar && snackBar?.isShown!!)
                snackBar?.dismiss()

            snackBar  = Snackbar.make(frameLayout, "No network available", Snackbar.LENGTH_INDEFINITE)
                .setAction("Ok") {
                    pdr_btn_ok.isEnabled = false
                    ml_progressbar.visibility = View.VISIBLE
                    getMoviesIDs(startDate, endDate)
                }
            snackBar!!.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.movie_list_page_menu, menu)
        return true
        //return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_date_range -> {
                if (pdr_cl_parent.visibility == View.GONE) {
                    pdr_cl_parent.visibility = View.VISIBLE
                    pdr_cl_parent.startAnimation(slideDownAnimation)
                } else {
                    pdr_cl_parent.startAnimation(slideUpAnimation)
                }
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setupRecyclerView(recyclerView: RecyclerView, moviesIds: MovieIDs) {
        //recyclerView.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.grid_margin)))
        val gridAutoFitLayoutManager = GridAutofitLayoutManager(this, resources.getDimensionPixelSize(R.dimen.poster_width))
        if (isTab)
            recyclerView.layoutManager =
                GridAutofitLayoutManager(this, resources.getDimensionPixelSize(R.dimen.poster_height), LinearLayoutManager.HORIZONTAL, false)
        else recyclerView.layoutManager = gridAutoFitLayoutManager
        recyclerView.adapter = MovieItemRecyclerViewAdapter(this, moviesIds)
    }

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd"
    }
}
