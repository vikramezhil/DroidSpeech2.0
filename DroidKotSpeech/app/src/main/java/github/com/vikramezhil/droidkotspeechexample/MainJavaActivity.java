package github.com.vikramezhil.droidkotspeechexample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import github.com.vikramezhil.dks.speech.Dks;
import github.com.vikramezhil.dks.speech.DksListener;

/**
 * Droid Kotlin Speech Example Activity
 * @author vikramezhil
 */

public class MainJavaActivity extends AppCompatActivity {

    private Dks dks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dks = new Dks(getApplication(), getSupportFragmentManager(), new DksListener() {
            @Override
            public void onDksLiveSpeechResult(@NotNull String liveSpeechResult) {
                Log.d(getPackageName(), "Speech result - " + liveSpeechResult);
            }

            @Override
            public void onDksFinalSpeechResult(@NotNull String speechResult) {
                Log.d(getPackageName(), "Final Speech result - " + speechResult);
            }

            @Override
            public void onDksLiveSpeechFrequency(float frequency) {}

            @Override
            public void onDksLanguagesAvailable(@org.jetbrains.annotations.Nullable String defaultLanguage, @org.jetbrains.annotations.Nullable ArrayList<String> supportedLanguages) {
                Log.d(getPackageName(), "defaultLanguage - " + defaultLanguage);
                Log.d(getPackageName(), "supportedLanguages - " + supportedLanguages);

                if (supportedLanguages != null && supportedLanguages.contains("en-IN")) {
                    // Setting the speech recognition language to english india if found
                    dks.setCurrentSpeechLanguage("en-IN");
                }
            }

            @Override
            public void onDksSpeechError(@NotNull String errMsg) {
                Toast.makeText(getApplication(), errMsg, Toast.LENGTH_SHORT).show();
            }
        });

        dks.injectProgressView(R.layout.layout_pv_inject);
        dks.setOneStepResultVerify(true);

        MaterialButton btnStartDks = findViewById(R.id.btn_start_dks);
        btnStartDks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dks.startSpeechRecognition();
            }
        });
    }
}
