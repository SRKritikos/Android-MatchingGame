package sl.on.ca.comp208.imagematcher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import sl.on.ca.comp208.imagematcher.data.TopScoresDAO;
import sl.on.ca.comp208.imagematcher.model.ImageIds;
import sl.on.ca.comp208.imagematcher.model.UserScore;

public class MainActivity extends AppCompatActivity {
    Map<Integer, Integer> buttonImageIdMap;
    ImageIds imageIds;
    int score = 0;
    int numberOfGuesses = 0;
    int previousImageId = 0;
    long startTime = System.currentTimeMillis();
    int minutes = 0;
    int seconds = 0;
    int numberOfMatches = 0;
    ImageButton clickedButtonImage;
    TopScoresDAO topScoresDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("On create being called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imageIds = new ImageIds();
        this.topScoresDAO = new TopScoresDAO(this);
        this.buildImageModel();
        this.setUpTimeUpdater();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setContentView(R.layout.activity_main);
        this.buildImageModel();
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
                if (!this.buttonImageIdMap.get(previousImageId).equals( this.buttonImageIdMap.get(clickedImageId)) ) {
                    this.resetImageSides(clickedImageId, this.previousImageId);
                } else {
                    this.score += 10;
                    this.numberOfMatches++;
                    if (this.numberOfMatches * 2 == this.imageIds.getImageIds().size()) {
                        this.endGame();
                    }
                }
                this.previousImageId = 0;
            }
        }
    }

    private void endGame() {
        UserScore userScore = this.topScoresDAO.getUserScore(this.score, this.minutes, this.seconds);
        this.topScoresDAO.updateTopScores(userScore);
        ArrayList<UserScore> userTopScoreList = this.topScoresDAO.getTopUserScores();
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("topScores", userTopScoreList);
        intent.putExtra("score", score);
        intent.putExtra("numGuesses", numberOfGuesses);
        intent.putExtra("time", minutes+":"+seconds);
        this.finish();
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

    private void setUpTimeUpdater() {
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