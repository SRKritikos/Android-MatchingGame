package sl.on.ca.comp208.imagematcher;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sl.on.ca.comp208.imagematcher.adapter.TopScoresAdapter;
import sl.on.ca.comp208.imagematcher.model.UserScore;

public class ScoreActivity extends AppCompatActivity {
    ArrayList<UserScore> topUserScoresList;
    int score;
    int numGuesses;
    String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        this.score = this.getIntent().getExtras().getInt("score");
        this.numGuesses = this.getIntent().getExtras().getInt("numGuesses");
        this.time = this.getIntent().getExtras().getString("time");
        this.topUserScoresList = (ArrayList<UserScore>) this.getIntent().getSerializableExtra("topScores");
        TextView scoreText = (TextView) this.findViewById(R.id.finalScoreTxt);
        TextView numGuessesText = (TextView) this.findViewById(R.id.numGuessesTxt);
        TextView timeText  = (TextView) this.findViewById(R.id.timeTxt);
        scoreText.setText( String.valueOf(this.score) );
        timeText.setText(this.time);
        numGuessesText.setText( String.valueOf(this.numGuesses) );
        this.setUpTopScoresList();
    }

    private void setUpTopScoresList() {
        TopScoresAdapter topScoresAdapter = new TopScoresAdapter(this, topUserScoresList);
        ListView listView = (ListView) this.findViewById(R.id.topScoresListView);
        listView.setAdapter(topScoresAdapter);
    }
}
