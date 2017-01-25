package sl.on.ca.comp208.imagematcher.model;

import java.io.Serializable;

/**
 * Created by Steven on 1/23/2017.
 */

public class UserScore implements Serializable, Comparable<UserScore> {
    int minutes;
    int seconds;
    int score;

    public UserScore(int minutes, int seconds, int score) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.score = score;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(UserScore userScore) {
        int comparedUserScore = userScore.getScore() + userScore.getMinutes() + userScore.getSeconds();
        int currentUserScore = this.score + this.minutes + this.seconds;
        if (currentUserScore > comparedUserScore) {
            return -1;
        } else if (currentUserScore < comparedUserScore) {
            return 1;
        }
        return 0;
    }
}