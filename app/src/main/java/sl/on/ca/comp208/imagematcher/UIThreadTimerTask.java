package sl.on.ca.comp208.imagematcher;

import android.app.Activity;

import java.util.TimerTask;

/**
 * Created by Steven on 1/24/2017.
 */

public class UIThreadTimerTask extends TimerTask {
    private Activity activity;
    private IGoTime goTime;

    public UIThreadTimerTask(Activity activity, IGoTime goTime) {
        this.activity = activity;
        this.goTime = goTime;
    }

    @Override
    public void run() {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                goTime.go();
            }
        });
    }
}
