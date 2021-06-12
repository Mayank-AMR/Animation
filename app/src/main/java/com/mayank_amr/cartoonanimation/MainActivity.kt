package com.mayank_amr.cartoonanimation

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    //val titleBarHeight = 220

    // there is no 0th quadrant, to keep it simple the first value gets ignored

    lateinit var imageView: ImageView
    var startAngle: Double = 0.0
    var currentAngle: Double = 0.0

    var isAnimating = false
    var dialerHeight: Int = 0
    var dialerWidth: Int = 0
    lateinit var lipsyncAnimation: AnimationDrawable


    //private lateinit var mDetector: GestureDetectorCompat
    //var quadrantTouched = Array<Boolean>(5) { false }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resources.displayMetrics.let { displayMetrics ->
            dialerHeight = displayMetrics.heightPixels
            dialerWidth = displayMetrics.widthPixels

        }
        //mDetector = GestureDetectorCompat(this, MyGestureListener())


        imageView = findViewById<ImageView>(R.id.imageView)

        imageView.apply {
            setBackgroundResource(R.drawable.lipsync_animation)
            lipsyncAnimation = background as AnimationDrawable
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            when (isAnimating) {
                true -> stopLipsync()
                false -> startLipsync()
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // Finger touched the screen
                Log.d(TAG, "onTouchEvent ActionDown: X: ${event.x} Y: ${event.y}")

                currentAngle = getAngle(event.x, event.y)
                Log.d(TAG, "onTouchEvent ActionMove: currentAngle: $currentAngle")
                imageView.rotation = ((startAngle - currentAngle).toFloat())

                Log.d(TAG, "onTouchEvent ActionDown: StartAngle: $startAngle")

            }
            MotionEvent.ACTION_MOVE -> {
                // Finger moves on the screen.
                currentAngle = getAngle(event.x, event.y)
                Log.d(TAG, "onTouchEvent ActionMove: currentAngle: $currentAngle")
                imageView.rotation = ((startAngle - currentAngle).toFloat())
                //startAngle = currentAngle
                Log.d(TAG, "onTouchEvent ActionMove: Re StartAngle: $startAngle")


            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // Released the finger.

            }
        }
        return true
    }


    public fun getAngle(xTouch: Float, yTouch: Float): Double {


        val x: Double = xTouch - dialerWidth / 2.0
        val y: Double = dialerHeight - yTouch - dialerHeight / 2.0

        return when (getQuadrant(x, y)) {
            1 -> Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI
            2 -> 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI
            3 -> 180 + -1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI
            4 -> 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI
            else -> 0.0
        }
    }


    public fun getQuadrant(x: Double, y: Double): Int {
        return if (x >= 0) {
            if (y >= 0) 1 else 4
        } else {
            if (y >= 0) 2 else 3
        }
    }

    private fun startLipsync() {
        isAnimating = true
        lipsyncAnimation.start()
    }

    private fun stopLipsync() {
        isAnimating = false
        lipsyncAnimation.stop()
    }

}