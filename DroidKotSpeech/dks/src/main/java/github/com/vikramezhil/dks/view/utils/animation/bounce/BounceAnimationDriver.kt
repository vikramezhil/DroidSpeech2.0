package github.com.vikramezhil.dks.view.utils.animation.bounce

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import github.com.vikramezhil.dks.view.utils.animation.AnimationEngine

/**
 * Bounce Animation Driver
 * @author vikramezhil
 */

class BounceAnimationDriver(private val viewGroup: ViewGroup): AnimationEngine() {

    private val maxFrequency = 25f
    private val animDurationInMilliSecs = 500L

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
        viewGroup.forEachIndexed { index, view ->
            var translationFrequency = if (frequency <= 0) 1f else frequency * index

            if (translationFrequency > maxFrequency) { translationFrequency = maxFrequency }

            if (translationFrequency.toInt() % 2 == 0 ) { translationFrequency = -translationFrequency }

            val animation = ObjectAnimator.ofFloat(view, "translationY", translationFrequency, 0f)
            animation.duration = animDurationInMilliSecs
            animation.start()

            if (index == viewGroup.childCount - 1) {
                animation.addListener(object: Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {}

                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) { stop(true) }

                    override fun onAnimationCancel(animation: Animator?) { stop(true) }
                })
            }
        }
    }

    override fun idle() {
        viewGroup.forEachIndexed { index, view ->
            val animation = ObjectAnimator.ofFloat(view, "translationY", if (index % 2 == 0) -3f else 3f, 0f)
            animation.duration = animDurationInMilliSecs
            animation.repeatCount = ObjectAnimator.INFINITE
            animation.start()
        }
    }
}