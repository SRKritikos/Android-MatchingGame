package sl.on.ca.comp208.imagematcher.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sl.on.ca.comp208.imagematcher.model.UserScore;

/**
 * Created by Steven on 1/23/2017.
 */

public class TopScoresDataManager {
    private ArrayList<UserScore> topUserScores;
    private final int NUMBER_OF_TOP_SCORES = 5;
    SharedPreferences sharedPreferences;
    Gson gson;

    public TopScoresDataManager(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = new Gson();
        this.populateTopUserScores();
    }

    public UserScore getUserScore(int  score, int minutes, int seconds) {
        UserScore userScore = new UserScore(minutes, seconds, score);
        return  userScore;
    }

    private void populateTopUserScores() {
        String storedScores = this.sharedPreferences.getString("userScores", "" );
        if (storedScores.isEmpty()) {
            this.topUserScores = new ArrayList<>();
        } else {
            this.topUserScores = this.gson.fromJson(storedScores, new TypeToken<List<UserScore>>() {}.getType());
            this.sortUserScores();
        }
    }


    public List<UserScore> updateTopScores(UserScore newUserScore) {
        if (this.topUserScores.size() < NUMBER_OF_TOP_SCORES) {
            this.topUserScores.add(newUserScore);
        } else {
            this.topUserScores.remove( NUMBER_OF_TOP_SCORES - 1 );
            this.topUserScores.add(newUserScore);
        }
        this.sortUserScores();
        this.updatePreferences();
        return this.topUserScores;
    }

    private void updatePreferences() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        String jsonUserScores = this.gson.toJson(this.topUserScores);
        editor.putString("userScores", jsonUserScores);
        editor.commit();
    }


    private void sortUserScores() {
        Collections.sort(this.topUserScores);
    }

    public ArrayList<UserScore> getTopUserScores() {
        return topUserScores;
    }
}
