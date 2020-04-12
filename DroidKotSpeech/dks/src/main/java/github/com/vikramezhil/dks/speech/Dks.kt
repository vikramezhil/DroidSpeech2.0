package github.com.vikramezhil.dks.speech

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import github.com.vikramezhil.dks.R
import github.com.vikramezhil.dks.language.DksLanguageListener
import github.com.vikramezhil.dks.language.DksLanguageReceiver
import github.com.vikramezhil.dks.utils.isNetworkAvailable
import github.com.vikramezhil.dks.utils.parseSpeechResult
import github.com.vikramezhil.dks.view.alert.DksFullScreenDialog
import github.com.vikramezhil.dks.view.alert.DksFullScreenDialogListener
import github.com.vikramezhil.dks.view.alert.DksLiveObservers
import java.lang.Exception

/**
 * Droid Kotlin Speech
 * @author vikramezhil
 */

class Dks(private val app: Application, private val manager: FragmentManager?, private val listener: DksListener): DksEngine() {

    override var speechRecognizer: SpeechRecognizer? = SpeechRecognizer.createSpeechRecognizer(app)

    override var speechIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

    override var languageDetailsIntent: Intent = RecognizerIntent.getVoiceDetailsIntent(app)

    override var audioManager: AudioManager = app.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override var restartSpeechHandler: Handler = Handler()

    override var partialResultSpeechHandler: Handler = Handler()

    override var injectedProgressView: Runnable? = null

    override var listeningTime: Long = 0

    override var pauseAndSpeakTime: Long = 0

    override var finalSpeechResultFound: Boolean = false

    override var onReadyForSpeech: Boolean = false

    override var partialRestartActive: Boolean = false

    override var closedByUser: Boolean = false

    override var showProgressView: Boolean = false

    override var progressViewInactive: Boolean = true

    override var speechResult: MutableLiveData<String> = MutableLiveData()

    override var speechFrequency: MutableLiveData<Float> = MutableLiveData()

    override var speechOneStepVerify: MutableLiveData<Boolean> = MutableLiveData()

    override var ejectProgressView: MutableLiveData<Boolean> = MutableLiveData()

    override var resetActionButtons: MutableLiveData<Boolean> = MutableLiveData()

    override var currentSpeechLanguage: String? = null

    override var supportedSpeechLanguages: ArrayList<String>? = null

    override var continuousSpeechRecognition: Boolean = true

    override var oneStepResultVerify: Boolean = false

    init {
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, app.packageName)
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, maxVoiceResults)

        languageDetailsIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        app.sendOrderedBroadcast(languageDetailsIntent, null, DksLanguageReceiver(object: DksLanguageListener {
            override fun onDksSupportedLanguages(defaultLanguage: String?, supportedLanguages: ArrayList<String>?) {
                currentSpeechLanguage = defaultLanguage
                supportedSpeechLanguages = supportedLanguages

                // Sending an update once the languages are available
                listener.onDksLanguagesAvailable(defaultLanguage, supportedLanguages)
            }
        }), null, Activity.RESULT_OK, null, null)
    }

    override fun startSpeechRecognition() {
        if (partialRestartActive) partialRestartActive = false else speechResult.value = ""

        // Setting the current speech language if applicable
        if (currentSpeechLanguage != null && currentSpeechLanguage!!.isNotEmpty() && supportedSpeechLanguages != null  && supportedSpeechLanguages!!.contains(currentSpeechLanguage!!)) {
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, currentSpeechLanguage)
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, currentSpeechLanguage)
        }

        resetActionButtons.value = true
        onReadyForSpeech = false
        closedByUser = false

        if (app.isNetworkAvailable()) {
            if (showProgressView && progressViewInactive) injectedProgressView?.run()
            listeningTime = System.currentTimeMillis()
            pauseAndSpeakTime = listeningTime
            finalSpeechResultFound = false

            // Setting the speech recognizer listeners
            speechRecognizer?.setRecognitionListener(object: RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) { onReadyForSpeech = true }

                override fun onBeginningOfSpeech() {}

                override fun onRmsChanged(rmsdB: Float) {
                    // Sending an update with the live speech frequency
                    listener.onDksLiveSpeechFrequency(rmsdB)

                    if (showProgressView) speechFrequency.value = rmsdB
                }

                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {}

                override fun onResults(results: Bundle?) {
                    if (finalSpeechResultFound) return

                    // Final Speech result found
                    finalSpeechResultFound = true

                    // If audio was muted, resetting it back to normal
                    mute(false)

                    val result = results.parseSpeechResult()
                    if (result.valid) {
                        // Updating the speech observer with the speech result
                        speechResult.value = result.speechResult

                        if (showProgressView && oneStepResultVerify) {
                            speechOneStepVerify.value = true
                            closeSpeechOperations()
                        } else {
                            // Sending an update with the final speech result
                            listener.onDksFinalSpeechResult(result.speechResult)

                            if (continuousSpeechRecognition) {
                                // Starting the speech recognition if it is continuous
                                startSpeechRecognition()
                            } else {
                                // Closing the speech operations
                                closeSpeechOperations()
                            }
                        }
                    } else {
                        // No match found, restart speech recognition
                        restartSpeechRecognition(false)
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    if (finalSpeechResultFound) return

                    val result = partialResults.parseSpeechResult()
                    if (result.valid) {
                        // Sending an update with the live speech result
                        listener.onDksLiveSpeechResult(result.speechResult)

                        // Updating the speech observer with the live result
                        speechResult.value = result.speechResult

                        if ((System.currentTimeMillis() - pauseAndSpeakTime) > maxPauseTime) {
                            // Final Speech result found
                            finalSpeechResultFound = true

                            partialResultSpeechHandler.postDelayed({
                                // Closing the speech operations
                                closeSpeechOperations()

                                if (showProgressView && oneStepResultVerify) {
                                    speechOneStepVerify.value = true
                                } else {
                                    // Sending an update with the final speech result
                                    listener.onDksFinalSpeechResult(result.speechResult)

                                    if (continuousSpeechRecognition) {
                                        // Starting the speech recognition if it is continuous
                                        startSpeechRecognition()
                                    } else {
                                        // Closing the speech operations
                                        closeSpeechOperations()
                                    }
                                }
                            }, partialDelayTime)
                        } else {
                            pauseAndSpeakTime = System.currentTimeMillis()
                        }
                    } else {
                        pauseAndSpeakTime = System.currentTimeMillis()
                    }
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}

                override fun onError(error: Int) {
                    if (closedByUser) {
                        closedByUser = false
                        return
                    }

                    val errDuration = System.currentTimeMillis() - listeningTime

                    if (errDuration < errorTimeout && error == SpeechRecognizer.ERROR_NO_MATCH && !onReadyForSpeech) return

                    // Disabling/Enabling audio based on "audio beep disabled timeout",
                    mute(onReadyForSpeech && errDuration < audioBeepDisabledTimeout)

                    if (arrayOf(SpeechRecognizer.ERROR_NO_MATCH, SpeechRecognizer.ERROR_SPEECH_TIMEOUT, SpeechRecognizer.ERROR_AUDIO, SpeechRecognizer.ERROR_RECOGNIZER_BUSY).any { it == error }) {
                        // Restarting speech recognition
                        restartSpeechRecognition(error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY)
                    } else if (error < app.resources.getStringArray(R.array.dks_errors).size) {
                        listener.onDksSpeechError(app.resources.getStringArray(R.array.dks_errors)[error - 1])

                        // Resetting on ready for speech status
                        onReadyForSpeech = false

                        // Closing speech operations
                        closeSpeechOperations()
                    }
                }
            })

            // Cancel any running speech operations before listening
            cancelSpeechOperations()

            // Start listening to user speech
            speechRecognizer?.startListening(speechIntent)
        } else {
            // Internet not enabled
            listener.onDksSpeechError(app.resources.getString(R.string.dks_internet_not_enabled))

            // Closing the progress view if applicable
            if (showProgressView && !progressViewInactive) ejectProgressView.value = true
        }
    }

    override fun restartSpeechRecognition(partialRestart: Boolean) {
        restartSpeechHandler.postDelayed({
            if (closedByUser) {
                closedByUser = false

                // If audio beep was muted, resetting it back to normal
                mute(false)
            } else {
                partialRestartActive = partialRestart

                // Starting the speech recognition after a delay
                startSpeechRecognition()
            }
        }, maxPauseTime)
    }

    override fun cancelSpeechOperations() {
        // Cancels any running speech operations
        speechRecognizer?.cancel()
    }

    override fun closeSpeechOperations() {
        // Destroying the speech recognizer
        speechRecognizer?.destroy()

        // Resetting the mutable data
        speechOneStepVerify.value = false
        ejectProgressView.value = false
        resetActionButtons.value = false

        // Removing any running callbacks if applicable
        restartSpeechHandler.removeCallbacksAndMessages(null)
        partialResultSpeechHandler.removeCallbacksAndMessages(null)

        // If audio was muted, resetting it back to normal
        mute(false)
    }

    @Suppress("DEPRECATION")
    override fun mute(mute: Boolean) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, if (mute) AudioManager.ADJUST_MUTE else AudioManager.ADJUST_UNMUTE, 0)
            } else {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, mute)
            }
        } catch (e: Exception) {
            e.printStackTrace()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
            } else {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false)
            }
        }
    }

    override fun injectProgressView(progressViewLayout: Int) {
        showProgressView = true
        injectedProgressView = Runnable {
            triggerProgressView(progressViewLayout)
        }
    }

    override fun triggerProgressView(progressViewLayout: Int) {
        if (!showProgressView) return
        progressViewInactive = false

        val observers = DksLiveObservers(speechResult, speechFrequency, speechOneStepVerify, resetActionButtons, ejectProgressView)
        manager?.beginTransaction()?.add(DksFullScreenDialog(progressViewLayout, manager, observers, object: DksFullScreenDialogListener {
            override fun onSuccess(result: String) {
                // Sending an update on the final speech result
                listener.onDksFinalSpeechResult(result)
                if (continuousSpeechRecognition) startSpeechRecognition() else ejectProgressView.value = true
            }

            override fun onRetry() { startSpeechRecognition() }

            override fun onClose() {
                closedByUser = true
                progressViewInactive = true
                closeSpeechOperations()
            }

            override fun onPause() { closeSpeechOperations() }

            override fun onResume() { startSpeechRecognition() }

            override fun onMicrophonePermissionStatus(statusGiven: Boolean, restartSpeechOps: Boolean) {
                if (statusGiven) {
                    if (restartSpeechOps && !onReadyForSpeech) startSpeechRecognition()
                } else {
                    // Sending an update that the microphone permission is required
                    listener.onDksSpeechError(app.resources.getString(R.string.dks_mic_permissions_required))

                    closeSpeechOperations()
                    ejectProgressView.value = true
                }
            }
        }), DksFullScreenDialog.TAG)?.commit()
    }

    override fun ejectProgressView() {
        ejectProgressView.value = true
        injectedProgressView = null
        showProgressView = false
        progressViewInactive = true
    }
}