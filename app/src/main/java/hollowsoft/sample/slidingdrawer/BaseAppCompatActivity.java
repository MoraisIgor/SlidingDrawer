package hollowsoft.sample.slidingdrawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseAppCompatActivity extends AppCompatActivity implements Navigate {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass, final int flags) {

        final Intent intent = new Intent(this, activityClass);
        intent.setFlags(flags);

        startActivity(intent);
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass, final Bundle bundle) {

        final Intent intent = new Intent(this, activityClass);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass, final int flags, final Bundle bundle) {

        final Intent intent = new Intent(this, activityClass);
        intent.setFlags(flags);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode) {
        startActivityForResult(new Intent(this, activityClass), requestCode);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode,
                                  final int flags) {

        final Intent intent = new Intent(this, activityClass);
        intent.setFlags(flags);

        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode,
                                  final Bundle bundle) {

        final Intent intent = new Intent(this, activityClass);
        intent.putExtras(bundle);

        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode,
                                  final int flags, final Bundle bundle) {

        final Intent intent = new Intent(this, activityClass);
        intent.setFlags(flags);
        intent.putExtras(bundle);

        startActivityForResult(intent, requestCode);
    }
}
