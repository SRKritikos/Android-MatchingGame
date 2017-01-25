package sl.on.ca.comp208.imagematcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import sl.on.ca.comp208.imagematcher.data.TopScoresDataManager;
import sl.on.ca.comp208.imagematcher.model.ImageIds;
import sl.on.ca.comp208.imagematcher.model.UserScore;

public class MainActivity extends AppCompatActivity {
    final double PERCENT_PENALTY_PER_GUESS = .01D;
    final int BASE_SCORE = 100;

    Map<Integer, Integer> buttonImageIdMap;
    ImageIds imageIds;
    int score;
    int numberOfGuesses;
    int previousImageId;
    long startTime;
    int minutes;
    int seconds;
    int numberOfMatches;
    double percentPenalty;
    ImageButton clickedButtonImage;
    TopScoresDataManager topScoresDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imageIds = new ImageIds();
        this.topScoresDataManager = new TopScoresDataManager(this);
        this.initializeGameValues();
        this.buildImageModel();
        this.setUpTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setContentView(R.layout.activity_main);
        this.initializeGameValues();
        this.buildImageModel();
    }

    private void initializeGameValues() {
        this.score = 0;
        this.numberOfMatches = 0;
        this.numberOfGuesses = 0;
        this.startTime = System.currentTimeMillis();
        this.minutes = 0;
        this.seconds = 0;
        this.percentPenalty = 0D;
    }

    public void flipImage(View view) {
        this.numberOfGuesses++;
        this.clickedButtonImage = (ImageButton) view;
        int tag = Integer.parseInt( this.clickedButtonImage.getTag().toString() );
        if (tag == 0) {
            this.clickedButtonImage.setImageResource( buttonImageIdMap.get(clickedButtonImage.getId()) );
            this.clickedButtonImage.setTag(1);
            int clickedImageId = this.clickedButtonImage.getId();
            if (this.previousImageId == 0) {
                this.previousImageId = clickedImageId;
            } else {
                if (this.buttonImageIdMap.get(previousImageId).equals( this.buttonImageIdMap.get(clickedImageId)) ) {
                    this.score +=  this.BASE_SCORE - (this.BASE_SCORE * this.percentPenalty);
                    Log.i("CURRENT SCORE", "Score= " + this.score + "  Percent Penalty= " + this.percentPenalty);
                    this.numberOfMatches++;
                    if (this.numberOfMatches * 2 == this.imageIds.getImageIds().size()) {
                        this.endGame();
                    }
                } else {
                    this.percentPenalty += this.PERCENT_PENALTY_PER_GUESS;
                    this.resetImageSides(clickedImageId, this.previousImageId);
                }
                this.previousImageId = 0;
            }
        }
    }

    private void endGame() {
        UserScore userScore = this.topScoresDataManager.getUserScore(this.score, this.minutes, this.seconds);
        this.topScoresDataManager.updateTopScores(userScore);
        ArrayList<UserScore> userTopScoreList = this.topScoresDataManager.getTopUserScores();
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("topScores", userTopScoreList);
        intent.putExtra("score", score);
        intent.putExtra("numGuesses", numberOfGuesses);
        intent.putExtra("time", minutes+":"+seconds);
        startActivity(intent);
    }

    private void resetImageSides(final int currentImage, final int previousImage) {
        Timer timer = new Timer();
        timer.schedule(new UIThreadTimerTask(this, new IGoTime(){
            @Override
            public void go() {
                ImageButton currentImageButton = (ImageButton) findViewById(currentImage);
                currentImageButton.setImageResource(R.drawable.back_side);
                currentImageButton.setTag(0);
                ImageButton previousImageButton = (ImageButton) findViewById(previousImage);
                previousImageButton.setImageResource(R.drawable.back_side);
                previousImageButton.setTag(0);
            }
        }), 1000);
    }

    private void setUpTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new UIThreadTimerTask(this, new IGoTime() {
            @Override
            public void go() {
                long millis = System.currentTimeMillis() - startTime;
                seconds = (int) (millis / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                TextView textView = (TextView) findViewById(R.id.timerTxt);
                textView.setText( String.format("%d:%02d", minutes, seconds) );
            }
        }), 0, 100);
    }

    private void buildImageModel() {
        this.buttonImageIdMap = new HashMap<>();
        Collections.shuffle(this.imageIds.getImageIds());
        TableLayout tableLayout = (TableLayout) this.findViewById(R.id.imageTable);
        int imageIdPosition = 0;
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            if (tableLayout.getChildAt(i) instanceof TableRow) {
                TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                for (int x = 0; x < tableRow.getChildCount(); x++) {
                    if (tableRow.getChildAt(x) instanceof ImageButton) {
                        ImageButton imageButton = (ImageButton) tableRow.getChildAt(x);
                        int imageButtonId = imageButton.getId();
                        int imageId = this.imageIds.getImageIds().get(imageIdPosition);
                        this.buttonImageIdMap.put(imageButtonId, imageId);
                        imageIdPosition++;
                    }
                }
            }
        }
    }
}