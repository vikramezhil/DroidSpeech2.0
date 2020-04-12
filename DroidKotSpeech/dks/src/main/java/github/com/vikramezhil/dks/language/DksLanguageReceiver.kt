package github.com.vikramezhil.dks.language

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent

/**
 * Droid Kotlin Speech Language Receiver
 * @author vikramezhil
 */

class DksLanguageReceiver(private val listener: DksLanguageListener): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val results = getResultExtras(true)

            var defaultLanguage: String? = null
            if (results.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)) {
                defaultLanguage = results.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)
            }

            var supportedLanguages: ArrayList<String>? = null
            if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
                supportedLanguages = results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)?.toCollection(ArrayList())
            }

            // Sending an update on the supported languages
            listener.onDksSupportedLanguages(defaultLanguage, supportedLanguages)
        }
    }
}