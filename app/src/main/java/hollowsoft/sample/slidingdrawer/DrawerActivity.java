package hollowsoft.sample.slidingdrawer;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import hollowsoft.slidingdrawer.SlidingDrawer;
import hollowsoft.slidingdrawer.SlidingDrawerListener;

public abstract class DrawerActivity extends BaseAppCompatActivity implements SlidingDrawerListener {

    private static String TAG;


    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        TAG = this.getClass().getSimpleName();

        setContentView(this.getLayoutResourceId());

        final SlidingDrawer drawer = (SlidingDrawer) findViewById(R.id.drawer);
        final Switch wrapContentSwitch = (Switch) findViewById(R.id.wrap_content_switch);

        if (drawer != null) {
            drawer.setSlidingDrawerListener(this);
        }

        if (wrapContentSwitch != null) {
            wrapContentSwitch.setChecked(false);
            wrapContentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    DrawerActivity.this.toggleDrawerWrapContent();
                }
            });
        }
    }

    @Override
    public void onDrawerOpened() {
        Logger.logInfo(TAG, "Drawer is opened");
    }

    @Override
    public void onDrawerWillOpen() {
        Logger.logInfo(TAG, "Drawer will open");
    }

    @Override
    public void onDrawerClosed() {
        Logger.logInfo(TAG, "Drawer is closed");
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

    /**
     * Returns the ID of the layout that shall ba loaded
     * @return
     */
    public abstract int getLayoutResourceId();

    /**
     * Toggles the height (or width) sliding drawer between WRAP_CONTEXT and MATCH_PARENT.
     * If MATCH_PARENT, the drawer will deploy to use all of the parent height (in vertical mode) or width (in horizontal_mode).
     * If WRAP_CONTENT, it will deploy only to accommodate the height (width) of the handle & content views.
     */
    public abstract void toggleDrawerWrapContent();
}
