package github.com.vikramezhil.droidkotspeechexample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import github.com.vikramezhil.dks.view.progress.ProgressViewListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pv.setProgressViewListener(object: ProgressViewListener {
            override fun onClickedPositive(message: String) {
                val random = (1f + Math.random() * (25f - 1f)).toFloat()
                pv.setProgressBallFrequency(random)

                pv.setProgressMessage("Clicked on positive")
            }

            override fun onClickedNeutral() {
                pv.setProgressMessage("Clicked on neutral")
            }

            override fun onClickedNegative() {
                pv.setProgressMessage("Clicked on negative")
            }
        })
    }
}
