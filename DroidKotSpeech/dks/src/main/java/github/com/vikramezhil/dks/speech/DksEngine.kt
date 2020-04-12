package github.com.vikramezhil.dks.speech

import android.content.Intent
import android.media.AudioManager
import android.os.Handler
import android.speech.SpeechRecognizer
import androidx.lifecycle.MutableLiveData

/**
 * Droid Kotlin Speech Engine abstract
 * @author vikramezhil
 */

abstract class DksEngine {

    protected val maxVoiceResults = 5
    protected val maxPauseTime = 1000L
    protected val partialDelayTime = 500L
    protected val errorTimeout = 5000L
    protected val audioBeepDisabledTimeout = 30000L

    protected abstract var speechRecognizer: SpeechRecognizer?

    protected abstract var speechIntent: Intent

    protected abstract var languageDetailsIntent: Intent

    protected abstract var audioManager: AudioManager

    protected abstract var restartSpeechHandler: Handler

    protected abstract var partialResultSpeechHandler: Handler

    protected abstract var injectedProgressView: Runnable?

    protected abstract var listeningTime: Long

    protected abstract var pauseAndSpeakTime: Long

    protected abstract var finalSpeechResultFound: Boolean

    protected abstract var onReadyForSpeech: Boolean

    protected abstract var partialRestartActive: Boolean

    protected abstract var closedByUser: Boolean

    protected abstract var showProgressView: Boolean

    protected abstract var progressViewInactive: Boolean

    protected abstract var speechResult: MutableLiveData<String>

    protected abstract var speechFrequency: MutableLiveData<Float>

    protected abstract var speechOneStepVerify: MutableLiveData<Boolean>

    protected abstract var ejectProgressView: MutableLiveData<Boolean>

    protected abstract var resetActionButtons: MutableLiveData<Boolean>

    protected abstract var supportedSpeechLanguages: ArrayList<String>?

    abstract var currentSpeechLanguage: String?

    abstract var continuousSpeechRecognition: Boolean

    abstract var oneStepResultVerify: Boolean

    /**
     * Starts the speech recognition
     */
    abstract fun startSpeechRecognition()

    /**
     * Restarts the speech recognition
     * @param partialRestart The partial restart status
     */
    protected abstract fun restartSpeechRecognition(partialRestart: Boolean)

    /**
     * Cancels the speech operations
     */
    protected abstract fun cancelSpeechOperations()

    /**
     * Closes the speech operations
     */
    abstract fun closeSpeechOperations()

    /**
     * Mutes the audio
     * @param mute Boolean The mute audio status
     */
    protected abstract fun mute(mute: Boolean)

    /**
     * Injects the progress view
     * @param progressViewLayout The progress view layout
     */
    abstract fun injectProgressView(progressViewLayout: Int)

    /**
     * Triggers the progress view
     * @param progressViewLayout The progress view layout
     */
    protected abstract fun triggerProgressView(progressViewLayout: Int)

    /**
     * Ejects the progress view
     */
    abstract fun ejectProgressView()
}