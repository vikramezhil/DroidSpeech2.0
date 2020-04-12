package github.com.vikramezhil.droidkotspeechexample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import github.com.vikramezhil.dks.speech.Dks
import github.com.vikramezhil.dks.speech.DksListener
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Droid Kotlin Speech Example Activity
 * @author vikramezhil
 */

class MainActivity : AppCompatActivity() {

    private lateinit var dks: Dks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        dks = Dks(application, supportFragmentManager, object: DksListener {
            override fun onDksLiveSpeechResult(liveSpeechResult: String) {
                Log.d(application.packageName, "Speech result - $liveSpeechResult")
            }

            override fun onDksFinalSpeechResult(speechResult: String) {
                Log.d(application.packageName, "Final speech result - $speechResult")
            }

            override fun onDksLiveSpeechFrequency(frequency: Float) {}

            override fun onDksLanguagesAvailable(defaultLanguage: String?, supportedLanguages: ArrayList<String>?) {
                Log.d(application.packageName, "defaultLanguage - $defaultLanguage")
                Log.d(application.packageName, "supportedLanguages - $supportedLanguages")

                if (supportedLanguages != null && supportedLanguages.contains("ta-IN")) {
                    // Setting the speech recognition language to tamil if found
                    dks.currentSpeechLanguage = "ta-IN"
                }
            }

            override fun onDksSpeechError(errMsg: String) {
                Toast.makeText(application, errMsg, Toast.LENGTH_SHORT).show()
            }
        })

        dks.continuousSpeechRecognition = true
        dks.oneStepResultVerify = true
        dks.injectProgressView(R.layout.layout_pv_inject)

        btn_start_dks.setOnClickListener {
            dks.startSpeechRecognition()
        }
    }
}
