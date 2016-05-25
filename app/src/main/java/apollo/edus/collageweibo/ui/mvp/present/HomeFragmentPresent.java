package apollo.edus.collageweibo.ui.mvp.present;

import android.content.Context;

/**
 * Created by wenmingvs on 16/5/16.
 */
public interface HomeFragmentPresent {
    public void pullToRefreshData();

    public void requestMoreData();

    public void start();

    public void stop();
}
