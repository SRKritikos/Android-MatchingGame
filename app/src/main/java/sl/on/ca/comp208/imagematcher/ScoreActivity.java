package sl.on.ca.comp208.imagematcher;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        int score = this.getIntent().getExtras().getInt("score");
        Toast.makeText(this, "score: " + score, Toast.LENGTH_SHORT).show();
        Log.i("ScoreTag", ""+score);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String prefdata = preferences.getString("msg", "Default msg");
        Toast.makeText(this, prefdata, Toast.LENGTH_SHORT).show();
        Log.i("PrefDateTag", prefdata);
    }
}
