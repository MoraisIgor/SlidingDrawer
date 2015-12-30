package hollowsoft.sample.slidingdrawer;

import android.os.Bundle;

import hollowsoft.slidingdrawer.SlidingDrawer;
import hollowsoft.slidingdrawer.SlidingDrawerListener;

public class VerticalDrawerActivity extends BaseAppCompatActivity implements SlidingDrawerListener {

    private static final String TAG = VerticalDrawerActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.vertical_drawer_activity);

        final SlidingDrawer drawer = (SlidingDrawer) findViewById(R.id.vertical_drawer_activity_sliding_drawer_vertical_drawer);

        drawer.setSlidingDrawerListener(this);
    }

    @Override
    public void onDrawerOpened() {
        Logger.logInfo(TAG, "Drawer Opened");
    }

    @Override
    public void onDrawerWillOpen() {
        Logger.logInfo(TAG, "Drawer will open");
    }

    @Override
    public void onDrawerClosed() {
        Logger.logInfo(TAG, "Drawer Closed");
    }

    @Override
    public void onDrawerWilClose() {
        Logger.logInfo(TAG, "Drawer will close");
    }

    @Override
    public void onScrollStarted() {
        Logger.logInfo(TAG, "Scroll Started");
    }

    @Override
    public void onScrollEnded() {
        Logger.logInfo(TAG, "Scroll Ended");
    }
}
