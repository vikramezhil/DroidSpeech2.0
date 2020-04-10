package github.com.vikramezhil.dks.speech

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class DKSDriver(context: Context) {

    private var speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private var speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

    init {
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
    }

    
}