package hollowsoft.sample.slidingdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.main_fragment, viewGroup, false);
    }
}
