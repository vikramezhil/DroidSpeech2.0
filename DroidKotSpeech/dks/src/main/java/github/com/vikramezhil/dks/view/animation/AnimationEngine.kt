package github.com.vikramezhil.dks.view.animation

/**
 * Animation Engine abstract
 * @author vikramezhil
 */

abstract class AnimationEngine {

    protected val idleFrequency = 3f
    protected val minFrequencies = arrayOf(8f, 16f, 24f, 16f, 8f)
    protected val outOfBoundsMinFrequency = 0f
    protected val animDurationInMilliSecs = 500L

    protected abstract var animationRunning: Boolean

    /**
     * Starts the animation
     * @param frequency Float The animation frequency
     */
    abstract fun start(frequency: Float)

    /**
     * Stops the animation
     * @param switchToIdle Boolean The switch to idle mode status
     */
    abstract fun stop(switchToIdle: Boolean)

    /**
     * Starts animating the view
     * @param frequency Float The animation frequency
     */
    protected abstract fun animate(frequency: Float)

    /**
     * Starts idle mode for the view
     */
    protected abstract fun idle()
}