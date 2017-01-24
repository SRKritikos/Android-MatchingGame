package sl.on.ca.comp208.imagematcher.model;

/**
 * Created by Steven on 1/23/2017.
 */

public class UserScore {
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
}
