package com.jay.jayottsample

import android.app.Activity
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.AppBarLayout
import com.jay.jayottsample.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var anim = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        statusBarTransparent()
        initAppbar()
        initInsetMargin()

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

    private fun initAppbar() {
        binding.abl.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val topPadding = 120f.dpToPx(this)
            val abstractOffset = abs(verticalOffset)

            if (abstractOffset < topPadding) {
                binding.vToolbarBackground.alpha = 0f
                return@OnOffsetChangedListener
            }

            val verticalOffsetByToPadding = abstractOffset - topPadding
            val percentage = abs(verticalOffsetByToPadding) / appBarLayout.totalScrollRange

            binding.vToolbarBackground.alpha = 1 - if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2
        })

        initActionbar()
    }

    // I don't know why it's need this function
    private fun initInsetMargin() = with(binding) {
        ViewCompat.setOnApplyWindowInsetsListener(cdl) { v: View, insets: WindowInsetsCompat ->
            val params = v.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = insets.systemWindowInsetBottom

            ctlToolbarContainer.layoutParams = (ctlToolbarContainer.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0, insets.systemWindowInsetTop, 0, 0)
            }
            cptl.layoutParams = (cptl.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0, 0, 0, 0)
            }

            insets.consumeSystemWindowInsets()
        }
    }

    private fun initActionbar() = with(binding) {
        tb.navigationIcon = null
        tb.setContentInsetsAbsolute(0, 0)
        setSupportActionBar(binding.tb)
        supportActionBar?.let {
            it.setHomeButtonEnabled(false)
            it.setDisplayHomeAsUpEnabled(false)
            it.setDisplayShowHomeEnabled(false)
        }
    }

}

fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
}

fun Activity.statusBarTransparent() {
    window.apply {
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = Color.TRANSPARENT
    }
}