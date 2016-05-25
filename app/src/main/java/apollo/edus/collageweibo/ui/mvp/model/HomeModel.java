package apollo.edus.collageweibo.ui.mvp.model;

import com.android.volley.Response;
import com.baidu.location.BDLocation;

import java.util.List;

import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.user.EsUserProfile;

/**
 * Created by panyongqiang on 16/5/25.
 */
public interface HomeModel {

    interface OnStatusListFinishedListener {
        void noMoreData();

        void onDataFinish(List<WeiboResult.WeiboInfo> statuslist);

        void onError(String error);
    }

    //update userLocation
    public void updateUserLocation( Response.Listener listener, Response.ErrorListener errorListener);

    public void homeFirstWeiboList(OnStatusListFinishedListener listener);

    public void homeNextWeiboList(OnStatusListFinishedListener listener);

    public void reloadInfo();

    public EsUserProfile getProfile();

    public boolean isLogin();

    public void setLocation(BDLocation bdLocation);

}
