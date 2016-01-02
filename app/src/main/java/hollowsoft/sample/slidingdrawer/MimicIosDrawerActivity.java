package hollowsoft.sample.slidingdrawer;


import android.os.Bundle;

import hollowsoft.slidingdrawer.SlidingDrawerListener;

public class MimicIosDrawerActivity extends BaseAppCompatActivity
    implements SlidingDrawerListener {


    private static String TAG = MimicIosDrawerActivity.class.getSimpleName();;


    @Override
    protected void onCreate(final Bundle bundle) {

        super.onCreate(bundle);

        setContentView(R.layout.mimic_ios_drawer_activity);

    }

    public void onDrawerOpened() {
        Logger.logInfo(TAG, "Drawer Opened");
    }

    public void onDrawerWillOpen() {
        Logger.logInfo(TAG, "Drawer will open");
    }

    public void onDrawerClosed() {
        Logger.logInfo(TAG, "Drawer Closed");
    }

    public void onDrawerWilClose() {
        Logger.logInfo(TAG, "Drawer will close");
    }

    public void onScrollStarted() {
        Logger.logInfo(TAG, "Scroll Started");
    }

    public void onScrollEnded() {
        Logger.logInfo(TAG, "Scroll Ended");
    }
}
