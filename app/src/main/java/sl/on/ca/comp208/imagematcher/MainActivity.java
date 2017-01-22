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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import sl.on.ca.comp208.imagematcher.model.ImageIds;

public class MainActivity extends AppCompatActivity {
    Map<Integer, Integer> buttonImageIdMap;
    ImageIds imageIds;
    int numberOfGuesses = 0;
    int previousImageId = 0;
    ImageButton clickedButtonImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("On create being called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageIds = new ImageIds();
        this.buildImageModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.buildImageModel();
    }

    public void flipImage(View view) {
        this.numberOfGuesses++;
        clickedButtonImage = (ImageButton) view;
        int tag = Integer.parseInt( clickedButtonImage.getTag().toString() );
        if (tag == 0) {
            clickedButtonImage.setImageResource( buttonImageIdMap.get(clickedButtonImage.getId()) );
            clickedButtonImage.setTag(1);
            int clickedImageId = clickedButtonImage.getId();
            if (previousImageId == 0) {
                previousImageId = clickedImageId;
            } else {
                System.out.println(buttonImageIdMap.get(previousImageId) + " " + buttonImageIdMap.get(clickedImageId));
                if (!buttonImageIdMap.get(previousImageId).equals( buttonImageIdMap.get(clickedImageId)) ) {
                    resetImageSides(clickedImageId, previousImageId);
                } else {
                    //TODO : Update player score;
                }
                previousImageId = 0;
            }
        }
    }

    private void resetImageSides(int currentImage, int previousImage) {
        Timer timer = new Timer();
        timer.schedule(new PauseImageTimerTask(currentImage, previousImage), 1000);
    }

    public void goToScoreActivity(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("msg", "msg from main activity");
        editor.putInt("score", this.numberOfGuesses);
        editor.commit();
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("score", this.numberOfGuesses);
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
