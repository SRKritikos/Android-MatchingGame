package sl.on.ca.comp208.imagematcher;

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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import sl.on.ca.comp208.imagematcher.data.TopScoresDAO;
import sl.on.ca.comp208.imagematcher.model.ImageIds;

public class MainActivity extends AppCompatActivity {
    Map<Integer, Integer> buttonImageIdMap;
    ImageIds imageIds;
    int score = 0;
    int numberOfGuesses = 0;
    int previousImageId = 0;
    long startTime = 0;
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
                    resetImageSides(clickedImageId, this.previousImageId);
                } else {
                    this.score += 10;
                }
                this.previousImageId = 0;
            }
        }
    }

    private void resetImageSides(int currentImage, int previousImage) {
        Timer timer = new Timer();
        timer.schedule(new PauseImageTimerTask(currentImage, previousImage), 1000);
    }

    private void setUpTimeUpdater() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long millis = System.currentTimeMillis() - startTime;
                        int seconds = (int) (millis / 1000);
                        int minutes = seconds / 60;
                        seconds = seconds % 60;
                        TextView textView = (TextView) findViewById(R.id.timerTxt);
                        textView.setText(String.format("%d:%02d", minutes, seconds));
                    }
                });
            }
        },0, 100);

    }

    public void goToScoreActivity(View view) {
        //Refactor into dao
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("score", this.score);
        editor.putInt("numGuesses", this.numberOfGuesses);
        editor.commit();
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("numGuesses", this.numberOfGuesses);
        intent.putExtra("score", this.score);
        this.startActivity(intent);
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

    /**
     * Class to pause an image before next task is executed.
     */
    private class PauseImageTimerTask extends TimerTask {
        private int previousImage;
        private int currentImage;

        public PauseImageTimerTask(int currentImage, int previousImage) {
            this.currentImage = currentImage;
            this.previousImage = previousImage;
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageButton currentImageButton = (ImageButton) findViewById(currentImage);
                    currentImageButton.setImageResource(R.drawable.back_side);
                    currentImageButton.setTag(0);
                    ImageButton previousImageButton = (ImageButton) findViewById(previousImage);
                    previousImageButton.setImageResource(R.drawable.back_side);
                    previousImageButton.setTag(0);
                }
            });
        }
    }
}
