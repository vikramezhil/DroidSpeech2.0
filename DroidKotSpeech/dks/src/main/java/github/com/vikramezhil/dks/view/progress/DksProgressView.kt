package github.com.vikramezhil.dks.view.progress

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.forEachIndexed
import github.com.vikramezhil.dks.R
import github.com.vikramezhil.dks.view.animation.bounce.BounceAnimationDriver
import kotlinx.android.synthetic.main.layout_progress_view.view.*
import java.lang.Exception

/**
 * Dks Progress View
 * @author vikramezhil
 */

class DksProgressView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): FrameLayout(context) {

    private var dksProgressViewListener: DksProgressViewListener? = null
    private var animationDriver: BounceAnimationDriver

    init {
        View.inflate(context, R.layout.layout_progress_view, this)

        animationDriver = BounceAnimationDriver(context, ll_progress_balls)

        init(context, attrs)
    }

    /**
     * Initializes the view attributes
     * @param context Context The view context
     * @param attrs AttributeSet The view attributes
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.DksProgressView, 0, 0)

        try {
            // Background
            val pvBackgroundColor = typedArray.getInt(R.styleable.DksProgressView_pvBackgroundColor, Color.WHITE)
            val pvBackgroundAlpha = typedArray.getFloat(R.styleable.DksProgressView_pvBackgroundAlpha, 1f)
            setBackground(pvBackgroundColor, pvBackgroundAlpha)

            // Ball
            val pvBallColors = typedArray.getResourceId(R.styleable.DksProgressView_pvBallColors, 0)
            setProgressBallColors(typedArray.resources.getIntArray(pvBallColors).toCollection(ArrayList()))

            // Message
            var pvMessage = typedArray.getString(R.styleable.DksProgressView_pvMessage)
            if (pvMessage.isNullOrEmpty()) pvMessage = resources.getString(R.string.bounce_ball_content_desc)
            setProgressMessage(pvMessage)

            val pvMessageColor = typedArray.getInt(R.styleable.DksProgressView_pvMessageColor, Color.BLACK)
            setProgressMessageColor(pvMessageColor)

            val pvMessageTextSize = typedArray.getFloat(R.styleable.DksProgressView_pvMessageTextSize, 0f)
            setProgressMessageTextSize(pvMessageTextSize)

            // Positive Button
            var pvPositiveButtonText = typedArray.getString(R.styleable.DksProgressView_pvPositiveButtonText)
            if (pvPositiveButtonText.isNullOrEmpty()) pvPositiveButtonText = resources.getString(R.string.positive)
            setPositiveButtonText(pvPositiveButtonText)

            val pvPositiveButtonWidth = typedArray.getInt(R.styleable.DksProgressView_pvPositiveButtonWidth, 0)
            val pvPositiveButtonHeight = typedArray.getInt(R.styleable.DksProgressView_pvPositiveButtonHeight, 0)
            setPositiveButtonDimensions(pvPositiveButtonWidth, pvPositiveButtonHeight)

            val pvPositiveButtonTextSize = typedArray.getFloat(R.styleable.DksProgressView_pvPositiveButtonTextSize, 0f)
            setPositiveButtonTextSize(pvPositiveButtonTextSize)

            val pvPositiveButtonBackgroundColor = typedArray.getInt(R.styleable.DksProgressView_pvPositiveButtonBackgroundColor, Color.GRAY)
            val pvPositiveButtonTextColor = typedArray.getInt(R.styleable.DksProgressView_pvPositiveButtonTextColor, Color.BLACK)
            val pvPositiveButtonCornerRadius = typedArray.getInt(R.styleable.DksProgressView_pvPositiveButtonCornerRadius, 1)
            val pvPositiveButtonBackgroundAlpha = typedArray.getFloat(R.styleable.DksProgressView_pvPositiveButtonBackgroundAlpha, 1f)
            val pvPositiveButtonTextCaps = typedArray.getBoolean(R.styleable.DksProgressView_pvPositiveButtonTextCaps, false)
            setPositiveButtonProperties(pvPositiveButtonBackgroundColor, pvPositiveButtonTextColor, pvPositiveButtonCornerRadius, pvPositiveButtonBackgroundAlpha, pvPositiveButtonTextCaps)

            // Neutral Button
            var pvNeutralButtonText = typedArray.getString(R.styleable.DksProgressView_pvNeutralButtonText)
            if (pvNeutralButtonText.isNullOrEmpty()) pvNeutralButtonText = resources.getString(R.string.neutral)
            setNeutralButtonText(pvNeutralButtonText)

            val pvNeutralButtonWidth = typedArray.getInt(R.styleable.DksProgressView_pvNeutralButtonWidth, 0)
            val pvNeutralButtonHeight = typedArray.getInt(R.styleable.DksProgressView_pvNeutralButtonHeight, 0)
            setNeutralButtonDimensions(pvNeutralButtonWidth, pvNeutralButtonHeight)

            val pvNeutralButtonTextSize = typedArray.getFloat(R.styleable.DksProgressView_pvNeutralButtonTextSize, 0f)
            setNeutralButtonTextSize(pvNeutralButtonTextSize)

            val pvNeutralButtonBackgroundColor = typedArray.getInt(R.styleable.DksProgressView_pvNeutralButtonBackgroundColor, Color.GRAY)
            val pvNeutralButtonTextColor = typedArray.getInt(R.styleable.DksProgressView_pvNeutralButtonTextColor, Color.BLACK)
            val pvNeutralButtonCornerRadius = typedArray.getInt(R.styleable.DksProgressView_pvNeutralButtonCornerRadius, 1)
            val pvNeutralButtonBackgroundAlpha = typedArray.getFloat(R.styleable.DksProgressView_pvNeutralButtonBackgroundAlpha, 1f)
            val pvNeutralButtonTextCaps = typedArray.getBoolean(R.styleable.DksProgressView_pvNeutralButtonTextCaps, false)
            setNeutralButtonProperties(pvNeutralButtonBackgroundColor, pvNeutralButtonTextColor, pvNeutralButtonCornerRadius, pvNeutralButtonBackgroundAlpha, pvNeutralButtonTextCaps)

            // Negative Button
            var pvNegativeButtonText = typedArray.getString(R.styleable.DksProgressView_pvNegativeButtonText)
            if (pvNegativeButtonText.isNullOrEmpty()) pvNegativeButtonText = resources.getString(R.string.negative)
            setNegativeButtonText(pvNegativeButtonText)

            val pvNegativeButtonWidth = typedArray.getInt(R.styleable.DksProgressView_pvNegativeButtonWidth, 0)
            val pvNegativeButtonHeight = typedArray.getInt(R.styleable.DksProgressView_pvNegativeButtonHeight, 0)
            setNegativeButtonDimensions(pvNegativeButtonWidth, pvNegativeButtonHeight)

            val pvNegativeButtonTextSize = typedArray.getFloat(R.styleable.DksProgressView_pvNegativeButtonTextSize, 0f)
            setNegativeButtonTextSize(pvNegativeButtonTextSize)

            val pvNegativeButtonBackgroundColor = typedArray.getInt(R.styleable.DksProgressView_pvNegativeButtonBackgroundColor, Color.GRAY)
            val pvNegativeButtonTextColor = typedArray.getInt(R.styleable.DksProgressView_pvNegativeButtonTextColor, Color.BLACK)
            val pvNegativeButtonCornerRadius = typedArray.getInt(R.styleable.DksProgressView_pvNegativeButtonCornerRadius, 1)
            val pvNegativeButtonBackgroundAlpha = typedArray.getFloat(R.styleable.DksProgressView_pvNegativeButtonBackgroundAlpha, 1f)
            val pvNegativeButtonTextCaps = typedArray.getBoolean(R.styleable.DksProgressView_pvNegativeButtonTextCaps, false)
            setNegativeButtonProperties(pvNegativeButtonBackgroundColor, pvNegativeButtonTextColor, pvNegativeButtonCornerRadius, pvNegativeButtonBackgroundAlpha, pvNegativeButtonTextCaps)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    // Progress View Listener

    /**
     * Sets the progress view listener instance
     * @param dksProgressViewListener ProgressViewListener The class instance which implements the listener
     * @override onClickedPositive, onClickedNeutral, onClickedNegative
     */
    fun setProgressViewListener(dksProgressViewListener: DksProgressViewListener) {
        this.dksProgressViewListener = dksProgressViewListener

        btn_positive.setOnClickListener { this.dksProgressViewListener?.onClickedPositive() }
        btn_neutral.setOnClickListener { this.dksProgressViewListener?.onClickedNeutral() }
        btn_negative.setOnClickListener { this.dksProgressViewListener?.onClickedNegative() }
    }

    // Progress View Background

    /**
     * Sets the progress view background
     * @param bgColor Int The background color
     * @param alpha Float The alpha value
     */
    fun setBackground(bgColor: Int, alpha: Float) {
        viewBg.setBackgroundColor(bgColor)
        viewBg.alpha = alpha
    }

    // Progress View Message

    /**
     * Sets the progress message in the text view
     * @param text String The progress message to be set in the text view
     */
    fun setProgressMessage(text: String) {
        tv_progress_message.text = text
    }

    /**
     * Sets the progress message color
     * @param messageColor Int The progress message color
     */
    fun setProgressMessageColor(messageColor: Int) {
        tv_progress_message.setTextColor(messageColor)
    }

    /**
     * Sets the progress message text size
     * @param size Float The progress message text size
     */
    fun setProgressMessageTextSize(size: Float) {
        if (size > 0) {
            tv_progress_message.textSize = size
        }
    }

    // Progress View Positive Button

    /**
     * Sets the positive button text
     * @param text String The positive text to be shown in the button
     */
    fun setPositiveButtonText(text: String) {
        btn_positive.text = text
    }

    /**
     * Sets the positive button text size
     * @param size Float The positive button text size
     */
    fun setPositiveButtonTextSize(size: Float) {
        if (size > 0) {
            btn_positive.textSize = size
        }
    }

    /**
     * Sets the positive button properties
     * @param bgColor Int The background color
     * @param txtColor Int The text color
     * @param cornerRadius Int The corner radius
     * @param alpha Float The alpha value
     * @param isAllCaps Boolean The all caps status
     */
    fun setPositiveButtonProperties(bgColor: Int, txtColor: Int, cornerRadius: Int, alpha: Float, isAllCaps: Boolean) {
        btn_positive.setBackgroundColor(bgColor)
        btn_positive.setTextColor(txtColor)
        btn_positive.cornerRadius = cornerRadius
        btn_positive.alpha = alpha
        btn_positive.isAllCaps = isAllCaps
    }

    /**
     * Sets the positive button dimensions (width and height)
     * @param width Int The button width
     * @param height Int The button height
     */
    fun setPositiveButtonDimensions(width: Int, height: Int) {
        val params = btn_positive.layoutParams

        if (width > 0) {
            btn_positive.width = width
        }

        if (height > 0) {
            btn_positive.height = height
        }

        btn_positive.layoutParams = params
    }

    /**
     * Sets the positive button visibility
     * @param visibility One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}.
     */
    fun setPositiveButtonVisibility(visibility: Int) {
        btn_positive.visibility = visibility
    }

    // Progress View Neutral Button

    /**
     * Sets the neutral button text
     * @param text String The neutral text to be shown in the button
     */
    fun setNeutralButtonText(text: String) {
        btn_neutral.text = text
    }

    /**
     * Sets the neutral button text size
     * @param size Float The neutral button text size
     */
    fun setNeutralButtonTextSize(size: Float) {
        if (size > 0) {
            btn_neutral.textSize = size
        }
    }

    /**
     * Sets the neutral button properties
     * @param bgColor Int The background color
     * @param txtColor Int The text color
     * @param cornerRadius Int The corner radius
     * @param alpha Float The alpha value
     * @param isAllCaps Boolean The all caps status
     */
    fun setNeutralButtonProperties(bgColor: Int, txtColor: Int, cornerRadius: Int, alpha: Float, isAllCaps: Boolean) {
        btn_neutral.setBackgroundColor(bgColor)
        btn_neutral.setTextColor(txtColor)
        btn_neutral.cornerRadius = cornerRadius
        btn_neutral.alpha = alpha
        btn_neutral.isAllCaps = isAllCaps
    }

    /**
     * Sets the neutral button dimensions (width and height)
     * @param width Int The button width
     * @param height Int The button height
     */
    fun setNeutralButtonDimensions(width: Int, height: Int) {
        val params = btn_neutral.layoutParams

        if (width > 0) {
            btn_neutral.width = width
        }

        if (height > 0) {
            btn_neutral.height = height
        }

        btn_neutral.layoutParams = params
    }

    /**
     * Sets the neutral button visibility
     * @param visibility One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}.
     */
    fun setNeutralButtonVisibility(visibility: Int) {
        btn_neutral.visibility = visibility
    }

    // Progress View Negative Button

    /**
     * Sets the negative button text
     * @param visibility String The negative text to be shown in the button
     */
    fun setNegativeButtonText(visibility: String) {
        btn_negative.text = visibility
    }

    /**
     * Sets the negative button text size
     * @param size Float The negative button text size
     */
    fun setNegativeButtonTextSize(size: Float) {
        if (size > 0) {
            btn_negative.textSize = size
        }
    }

    /**
     * Sets the negative button properties
     * @param bgColor Int The background color
     * @param txtColor Int The text color
     * @param cornerRadius Int The corner radius
     * @param alpha Float The alpha value
     * @param isAllCaps Boolean The all caps status
     */
    fun setNegativeButtonProperties(bgColor: Int, txtColor: Int, cornerRadius: Int, alpha: Float, isAllCaps: Boolean) {
        btn_negative.setBackgroundColor(bgColor)
        btn_negative.setTextColor(txtColor)
        btn_negative.cornerRadius = cornerRadius
        btn_negative.alpha = alpha
        btn_negative.isAllCaps = isAllCaps
    }

    /**
     * Sets the negative button dimensions (width and height)
     * @param width Int The button width
     * @param height Int The button height
     */
    fun setNegativeButtonDimensions(width: Int, height: Int) {
        val params = btn_negative.layoutParams

        if (width > 0) {
            btn_negative.width = width
        }

        if (height > 0) {
            btn_negative.height = height
        }

        btn_negative.layoutParams = params
    }

    /**
     * Sets the negative button visibility
     * @param visibility One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}.
     */
    fun setNegativeButtonVisibility(visibility: Int) {
        btn_negative.visibility = visibility
    }

    // Progress View Ball

    /**
     * Sets the progress ball colors
     * @param progressBallColors ArrayList<Int> The progress bar color list
     */
    fun setProgressBallColors(progressBallColors: ArrayList<Int>) {
        ll_progress_balls.forEachIndexed { index, view ->
            if (index < progressBallColors.size) {
                (view as ImageView).setColorFilter(progressBallColors[index])
            }
        }
    }

    /**
     * Sets the progress ball frequency
     * @param rmsdB The frequency value
     */
    fun setProgressBallFrequency(rmsdB: Float) {
        animationDriver.start(rmsdB)
    }
}