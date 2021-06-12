package com.mayank_amr.cartoonanimation

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Math.PI
import kotlin.math.asin
import kotlin.math.hypot


class MainActivity : AppCompatActivity() {
    private lateinit var cartoonImageView: ImageView
    private lateinit var playPauseButton: Button

    private var startAngle: Double = 0.0
    private var currentAngle: Double = 0.0

    private var isAnimating = false
    private lateinit var lipsSyncAnimation: AnimationDrawable

    private var displayHeight: Int = 0
    private var displayWidth: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Getting the display height and width..
        resources.displayMetrics.let { displayMetrics ->
            displayHeight = displayMetrics.heightPixels
            displayWidth = displayMetrics.widthPixels
        }

        cartoonImageView = findViewById(R.id.cartoon_imageView)
        playPauseButton = findViewById(R.id.play_pause_button)

        // Initialise cartoonImageview and playPauseButton..
        initialiseCartoonImageView(cartoonImageView)
        initialisePlayPauseButton(playPauseButton)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // When Finger touched the screen..
                currentAngle = getAngle(event.x, event.y)
                cartoonImageView.rotation = ((startAngle - currentAngle).toFloat())
            }
            MotionEvent.ACTION_MOVE -> {
                // When Finger moves on the screen.
                currentAngle = getAngle(event.x, event.y)
                cartoonImageView.rotation = ((startAngle - currentAngle).toFloat())
            }
        }
        return true
    }

    private fun startLipsSync() {
        isAnimating = true
        lipsSyncAnimation.start()
    }

    private fun stopLipsSync() {
        isAnimating = false
        lipsSyncAnimation.stop()
    }


    @SuppressLint("SetTextI18n")
    private fun initialisePlayPauseButton(playPauseButton: Button?) {
        playPauseButton?.setOnClickListener {
            when (isAnimating) {
                true -> {
                    playPauseButton.text = "Play"
                    stopLipsSync()
                }
                false -> {
                    playPauseButton.text = "Stop"
                    startLipsSync()
                }
            }
        }
    }

    private fun initialiseCartoonImageView(cartoonImageView: ImageView?) {
        cartoonImageView?.apply {
            setBackgroundResource(R.drawable.lipsync_animation)
            lipsSyncAnimation = background as AnimationDrawable
        }
    }

    private fun getQuadrant(x: Double, y: Double): Int {
        return if (x >= 0) {
            if (y >= 0) 1 else 4
        } else {
            if (y >= 0) 2 else 3
        }
    }

    private fun getAngle(xTouch: Float, yTouch: Float): Double {
        val displayMidX: Double = xTouch - displayWidth / 2.0
        val displayMidY: Double = displayHeight - yTouch - displayHeight / 2.0

        return when (getQuadrant(displayMidX, displayMidY)) {
            1 -> asin(displayMidY / hypot(displayMidX, displayMidY)) * 180 / PI
            2 -> 180 - asin(displayMidY / hypot(displayMidX, displayMidY)) * 180 / PI
            3 -> 180 + -1 * asin(displayMidY / hypot(displayMidX, displayMidY)) * 180 / PI
            4 -> 360 + asin(displayMidY / hypot(displayMidX, displayMidY)) * 180 / PI
            else -> 0.0
        }
    }

}