package sl.on.ca.comp208.imagematcher.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sl.on.ca.comp208.imagematcher.model.UserScore;

/**
 * Created by Steven on 1/23/2017.
 */

public class TopScoresDAO {
    private List<UserScore> topUserScores;
    private final int NUMBER_OF_TOP_SCORES = 5;
    SharedPreferences sharedPreferences;

    public TopScoresDAO(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> scores = this.sharedPreferences.getStringSet("scores", new HashSet<String>());
        Set<String> minutes = this.sharedPreferences.getStringSet("minutes", new HashSet<String>());
        Set<String> seconds = this.sharedPreferences.getStringSet("seconds", new HashSet<String>());
        this.topUserScores = new ArrayList<>();
        this.populateTopUserScores(scores, minutes, seconds);
    }

    public UserScore getUserScore(int  score, int minutes, int seconds) {
        UserScore userScore = new UserScore(minutes, seconds, score);
        return  userScore;
    }

    private void populateTopUserScores(Set<String> scores, Set<String> minutes, Set<String> seconds) {
        if (!scores.isEmpty() && !minutes.isEmpty() && !seconds.isEmpty()) {
            List<String> scoresList = new ArrayList<>(scores);
            List<String> minutesList = new ArrayList<>(minutes);
            List<String> secondsList = new ArrayList<>(seconds);
            for (int i = 0; i < scores.size(); i++) {
                int score = Integer.parseInt(scoresList.get(i));
                int minute = Integer.parseInt(minutesList.get(i));
                int second = Integer.parseInt(secondsList.get(i));
                UserScore userScore = new UserScore(minute, second, score);
                this.topUserScores.add(userScore);
            }
            this.sortUserScoresByScore();
        }
    }


    public List<UserScore> updateTopScores(UserScore newUserScore) {
        for (UserScore userScore : this.topUserScores) {
            if (newUserScore.getScore() > userScore.getScore()) {
                this.topUserScores.remove(userScore);
                this.topUserScores.add(newUserScore);
                break;
            }
        }
        this.sortUserScoresByScore();
        this.updatePreferences();
        return this.topUserScores;
    }

    private void updatePreferences() {
        Set<String> scoresSet = new HashSet<>();
        Set<String> minutesSet = new HashSet<>();
        Set<String> secondsSet = new HashSet<>();
        for (UserScore userScore : this.topUserScores) {
            scoresSet.add( String.valueOf(userScore.getScore()) );
            minutesSet.add( String.valueOf(userScore.getMinutes()) );
            secondsSet.add( String.valueOf(userScore.getSeconds()) );
        }

        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putStringSet("scores", scoresSet);
        editor.putStringSet("minutes", minutesSet);
        editor.putStringSet("seconds", secondsSet);
        editor.commit();
    }


    private void sortUserScoresByScore() {
        Collections.sort(this.topUserScores, new Comparator<UserScore>() {
            @Override
            public int compare(UserScore userScore, UserScore t1) {
                return userScore.getScore() > t1.getScore() ? 1 : -1;
            }
        });
    }

    public List<UserScore> getTopUserScores() {
        return topUserScores;
    }
}
