package hollowsoft.sample.slidingdrawer;

import android.app.Activity;
import android.os.Bundle;

public interface Navigate {

    void navigateTo(Class<? extends Activity> activityClass);

    void navigateTo(Class<? extends Activity> activityClass, int flags);

    void navigateTo(Class<? extends Activity> activityClass, Bundle bundle);

    void navigateTo(Class<? extends Activity> activityClass, int flags, Bundle bundle);

    void navigateForResult(Class<? extends Activity> activityClass, int requestCode);

    void navigateForResult(Class<? extends Activity> activityClass, int requestCode, int flags);

    void navigateForResult(Class<? extends Activity> activityClass, int requestCode, Bundle bundle);

    void navigateForResult(Class<? extends Activity> activityClass, int requestCode, int flags, Bundle bundle);
}
