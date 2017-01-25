package sl.on.ca.comp208.imagematcher;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sl.on.ca.comp208.imagematcher.model.UserScore;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        int score = this.getIntent().getExtras().getInt("score");
        int numGuesses = this.getIntent().getExtras().getInt("numGuesses");
        String time = this.getIntent().getExtras().getString("time");
        ArrayList<UserScore> topUserScoresList = (ArrayList<UserScore>) this.getIntent().getSerializableExtra("topScores");
        Log.i("Score", "ListSize: "+topUserScoresList.size());
        for (UserScore userScore : topUserScoresList) {
            Log.i("Score", "Minutes: "+userScore.getSeconds());
        }
        TextView scoreText = (TextView) this.findViewById(R.id.finalScoreTxt);
        TextView numGuessesText = (TextView) this.findViewById(R.id.numGuessesTxt);
        TextView timeText  = (TextView) this.findViewById(R.id.timeTxt);
        scoreText.setText( String.valueOf(score) );
        timeText.setText(time);
        numGuessesText.setText( String.valueOf(numGuesses) );
    }
}
