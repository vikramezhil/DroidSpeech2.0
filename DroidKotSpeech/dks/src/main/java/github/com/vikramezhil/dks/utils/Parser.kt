package github.com.vikramezhil.dks.utils

import android.content.Context
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.util.DisplayMetrics
import kotlin.math.roundToInt

/**
 * Parser Extensions
 * @author vikramezhil
 */

data class SpeechResult(val valid : Boolean, val speechResult : String)

/**
 * Parses the bundle speech result
 * @receiver Bundle? The speech bundle
 * @return Boolean The bundle speech valid status and result
 */
fun Bundle?.parseSpeechResult(): SpeechResult {
    if (this == null) return SpeechResult(false, "")
    if (!this.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) return SpeechResult(false, "")
    if (this.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) == null) return SpeechResult(false, "")

    val result = this.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)!!

    if (result.size > 0 && result[0].trim().isNotEmpty()) {
        return SpeechResult(true, result[0].trim())
    } else {
        return SpeechResult(false, "")
    }
}

/**
 * Converts pixels to dp
 * @receiver Int The pixels value
 * @param context Context The context instance
 * @return Int The converted dp value
 */
fun Int.pxToDp(context: Context): Int {
    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    return (this / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt();
}