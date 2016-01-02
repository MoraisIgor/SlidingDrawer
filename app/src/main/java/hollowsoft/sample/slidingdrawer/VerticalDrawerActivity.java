package hollowsoft.sample.slidingdrawer;


import android.view.ViewGroup;

import hollowsoft.slidingdrawer.SlidingDrawer;

public class VerticalDrawerActivity extends DrawerActivity {


    @Override
    public int getLayoutResourceId() {
        return R.layout.vertical_drawer_activity;
    }

    @Override
    public void toggleDrawerWrapContent() {

        final SlidingDrawer drawer = (SlidingDrawer) findViewById(R.id.drawer);

        if (drawer != null) {

            if (drawer.isOpened() || drawer.isMoving()) {
                drawer.close();
            }

            ViewGroup.LayoutParams layoutParams = drawer.getLayoutParams();

            if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            drawer.setLayoutParams(layoutParams);

        }
    }

}
