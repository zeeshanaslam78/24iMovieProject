package com.xeeshi.a24imovieproject

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_trailer_playback.*
import kotlinx.android.synthetic.main.exo_playback_control_view_custom.*
import timber.log.Timber


class TrailerPlaybackActivity : AppCompatActivity() {

    internal lateinit var exoPlayer: SimpleExoPlayer

    private var currentVolume = 0f
    private var isVolumeUp = true

    private val videoUriFromIntent: String
        get() {
            val url = intent.getStringExtra(ARG_TRAILER_URL)
            Timber.d(TAG, "url %s", url)
            return url
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer_playback)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.d(TAG, "onCreate Called")

        Timber.i("videoUri %s", videoUriFromIntent)

        val btnClose = findViewById<ImageView>(R.id.btn_close)
        btnClose.setOnClickListener { finish() }

        imgBtn_volume.setOnClickListener {
            if (isVolumeUp) {
                isVolumeUp = false
                imgBtn_volume.setImageResource(R.drawable.ic_volume_up_white_24dp)
                exoPlayer.volume = currentVolume

            } else {
                isVolumeUp = true
                imgBtn_volume.setImageResource(R.drawable.ic_volume_off_white_24dp)
                exoPlayer.volume = 0f

            }
        }

        val trackSelector = DefaultTrackSelector(BANDWIDTH_METER)
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        exoplayer_view.useController = true
        exoplayer_view.player = exoPlayer


        // Produces DataSource instances through which media data is loaded.
        val defaultDataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "Movie Trailers"),
            BANDWIDTH_METER
        )

        val mp4VideoUri = Uri.parse(videoUriFromIntent)
        val mediaSource: MediaSource
        if (videoUriFromIntent.toUpperCase().contains("M3U8")) {
            mediaSource = HlsMediaSource(mp4VideoUri, defaultDataSourceFactory, null, null)
        } else {
            mediaSource = ExtractorMediaSource(mp4VideoUri, defaultDataSourceFactory, DefaultExtractorsFactory(), null, null)
        }
        //val mediaSource = HlsMediaSource.Factory(defaultDataSourceFactory).createMediaSource(videoUri)
        //final LoopingMediaSource loopingMediaSource = new LoopingMediaSource(ms);

        // Prepare the player with the source.
        exoPlayer.prepare(mediaSource)

        exoPlayer.playWhenReady = true
        currentVolume = exoPlayer.volume


        exoPlayer.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
                Timber.d(TAG, "onTimelineChanged timeline %s", timeline?.periodCount)
            }

            override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
                Timber.d(TAG, "onTracksChanged")
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                Timber.d(TAG, "onLoadingChanged isLoading %s", isLoading)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                Timber.d(TAG, "onPlayerStateChanged playWhenReady $playWhenReady playbackState $playbackState")
                when (playbackState) {
                    Player.STATE_BUFFERING -> progressbar.visibility = View.VISIBLE
                    Player.STATE_ENDED -> exoPlayer.stop()
                    else -> progressbar.visibility = View.GONE
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                Timber.d(TAG, "onRepeatModeChanged")
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                Timber.d(TAG, "onShuffleModeEnabledChanged")
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                Timber.d(TAG, "onPlayerError")
                exoPlayer.stop()

                exoPlayer.playWhenReady = true
                currentVolume = exoPlayer.volume
            }

            override fun onPositionDiscontinuity(reason: Int) {
                Timber.d(TAG, "onPositionDiscontinuity")
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                Timber.d(TAG, "onPlaybackParametersChanged playbackParameters %s", playbackParameters.speed)
            }

            override fun onSeekProcessed() {
                Timber.d(TAG, "onSeekProcessed")
            }
        })


        swipableLayout.setOnLayoutCloseListener { finish() }


    }

    override fun onPause() {
        super.onPause()
        exoPlayer.playWhenReady = false
    }

    override fun onResume() {
        super.onResume()
        exoPlayer.playWhenReady = true
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }



    companion object {
        private val BANDWIDTH_METER = DefaultBandwidthMeter()
        const val ARG_TRAILER_URL = "trailer_url"
        private val TAG = TrailerPlaybackActivity::class.java.simpleName
    }
}