package github.com.vikramezhil.dks.view.animation.bounce

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import github.com.vikramezhil.dks.utils.pxToDp
import github.com.vikramezhil.dks.view.animation.AnimationEngine

/**
 * Bounce Animation Driver
 * @author vikramezhil
 */

class BounceAnimationDriver(private val context: Context, private val viewGroup: ViewGroup): AnimationEngine() {

    override var animationRunning: Boolean = false

    init {
        idle()
    }

    override fun start(frequency: Float) {
        animate(frequency)
    }

    override fun stop(switchToIdle: Boolean) {
        viewGroup.forEach {
            it.clearAnimation()
        }

        if (switchToIdle) idle()
    }

    override fun animate(frequency: Float) {
        if (frequency <= outOfBoundsMinFrequency || animationRunning) return

        animationRunning = true

        val viewGroupHeight = viewGroup.height.pxToDp(context).toFloat()
        viewGroup.forEachIndexed { index, view ->
            val bounceTransitionAnimation = when {
                index > minFrequencies.size -> {
                    viewGroupHeight
                }
                (minFrequencies[index] * frequency) > viewGroupHeight -> {
                    viewGroupHeight
                }
                else -> {
                    minFrequencies[index] * frequency
                }
            }

            val animation = ObjectAnimator.ofFloat(view, "translationY", -(bounceTransitionAnimation), 0f)
            animation.duration = animDurationInMilliSecs
            animation.start()

            if (index == viewGroup.childCount - 1) {
                animation.addListener(object: Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {}

                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        animationRunning = false
                        stop(true)
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        animationRunning = false
                        stop(true)
                    }
                })
            }
        }
    }

    override fun idle() {
        viewGroup.forEachIndexed { index, view ->
            val animation = ObjectAnimator.ofFloat(view, "translationY", if (index % 2 == 0) -(idleFrequency) else idleFrequency, 0f)
            animation.duration = animDurationInMilliSecs
            animation.repeatCount = ObjectAnimator.INFINITE
            animation.start()
        }
    }
}