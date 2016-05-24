package apollo.edus.collageweibo.ui.mvp.present;

import android.content.Context;

/**
 * Created by wenmingvs on 16/5/16.
 */
public interface FriendActivityPresent {
    public void pullToRefreshData(String uid, Context context);

    public void requestMoreData(String uid, Context context);
}
