package hollowsoft.sample.slidingdrawer;

import android.os.Bundle;

import hollowsoft.slidingdrawer.OnDrawerCloseListener;
import hollowsoft.slidingdrawer.OnDrawerOpenListener;
import hollowsoft.slidingdrawer.OnDrawerScrollListener;
import hollowsoft.slidingdrawer.SlidingDrawer;

public class HorizontalDrawerActivity extends BaseAppCompatActivity implements OnDrawerOpenListener,
                                                                               OnDrawerCloseListener,
                                                                               OnDrawerScrollListener {

    private static final String TAG = HorizontalDrawerActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.horizontal_drawer_activity);

        final SlidingDrawer drawer = (SlidingDrawer) findViewById(R.id.horizontal_drawer_activity_sliding_drawer_horizontal_drawer);

        drawer.setOnDrawerOpenListener(this);
        drawer.setOnDrawerCloseListener(this);
        drawer.setOnDrawerScrollListener(this);
    }

    @Override
    public void onDrawerOpened() {
        Logger.logInfo(TAG, "Drawer Opened");
    }

    @Override
    public void onDrawerClosed() {
        Logger.logInfo(TAG, "Drawer Closed");
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
