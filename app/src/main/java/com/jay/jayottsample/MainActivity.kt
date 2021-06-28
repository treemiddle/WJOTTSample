package com.jay.jayottsample

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.constraintlayout.motion.widget.MotionLayout
import com.jay.jayottsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var anim = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.sv.viewTreeObserver.addOnScrollChangedListener {
            if (binding.sv.scrollY > 150f.dpToPx(this).toInt()) {
                if (anim.not()) {
                    binding.mtlIv.transitionToEnd()
                    binding.mtlBtn.transitionToEnd()
                }
            } else {
                if (anim.not()) {
                    binding.mtlIv.transitionToStart()
                    binding.mtlBtn.transitionToStart()
                }
            }
        }

        binding.mtlIv.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                anim = true
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) = Unit
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                anim = false
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) = Unit
        })
    }

    fun Float.dpToPx(context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
    }
}