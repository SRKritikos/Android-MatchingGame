package sl.on.ca.comp208.imagematcher;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        int score = this.getIntent().getExtras().getInt("score");
        int numGuesses = this.getIntent().getExtras().getInt("numGuesses");

        TextView scoreText = (TextView) this.findViewById(R.id.finalScoreTxt);
        TextView numGuessesText = (TextView) this.findViewById(R.id.numGuessesTxt);

        scoreText.setText( String.valueOf(score) );
        numGuessesText.setText( String.valueOf(numGuesses) );
    }
}
