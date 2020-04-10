package github.com.vikramezhil.dks.view.utils.animation

/**
 * Animation Engine Methods
 * @author vikramezhil
 */

abstract class AnimationEngine {

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