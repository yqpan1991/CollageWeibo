package apollo.edus.collageweibo.ui.mvp.impl;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.global.EsGlobal;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.ui.mvp.model.HomeModel;
import apollo.edus.collageweibo.utils.ToastUtil;

/**
 * Created by panyongqiang on 16/5/25.
 */
public class HomeModelImpl implements HomeModel {
    private final String TAG = this.getClass().getSimpleName();
    private EsUserProfile mProfile;
    private boolean mHasLogin;
    private List<WeiboResult.WeiboInfo> mWeiboList = new ArrayList<>();
    private BDLocation mBDLocation;
    private String longtitude;
    private String latitude;
    private int currentRequestIndex = 0;

    public HomeModelImpl(){
        longtitude = "";
        latitude = "";
    }

    @Override
    public void updateUserLocation(Response.Listener listener, Response.ErrorListener errorListener) {
        String userId = mProfile != null ? mProfile.getUserId(): "";
        if(!mHasLogin || TextUtils.isEmpty(userId)){
            return;
        }
        EsApiHelper.updateUserLocation(userId, longtitude, latitude, listener, errorListener);
    }

    @Override
    public void homeFirstWeiboList(final OnStatusListFinishedListener listener) {
        String userId = mProfile != null ? mProfile.getUserId(): "";
        userId = mHasLogin ? userId : "";
        EsApiHelper.getHomePage(userId, longtitude, latitude, 0 ,new Response.Listener<String>(){

            @Override
            public void onResponse(String s) {
                currentRequestIndex = 0;
                mWeiboList.clear();
                Gson gson = new Gson();
                Log.e(TAG,"homeFirstWeiboList result:"+s);
                WeiboResult weiboResult = gson.fromJson(s, WeiboResult.class);
//                WeiboResult weiboResult = new WeiboResult();
                //TODO ------暂时更改为空结果集,接口数据需要调整-----
                List<WeiboResult.WeiboInfo> weiboResultList = weiboResult.getList();
//                List<WeiboResult.WeiboInfo> weiboResultList = new ArrayList<WeiboResult.WeiboInfo>();
                if(weiboResultList != null && !weiboResultList.isEmpty()){
                    mWeiboList.addAll(weiboResultList);
                    listener.onDataFinish(mWeiboList);
                }
                if(!weiboResult.hasNextPage() || weiboResultList == null || weiboResultList.isEmpty()){
                    listener.noMoreData();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showShort(EsGlobal.getGlobalContext(), volleyError.toString());
                listener.onError(volleyError.toString());
            }
        });
    }

    @Override
    public void homeNextWeiboList(final OnStatusListFinishedListener listener) {
        String userId = mProfile != null ? mProfile.getUserId(): "";
        userId = mHasLogin ? userId : "";
        currentRequestIndex++;
        EsApiHelper.getHomePage(userId ,longtitude, latitude, currentRequestIndex, new Response.Listener<String>(){

            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                Log.e(TAG,"homeNextWeiboList result:"+s);
                WeiboResult weiboResult = gson.fromJson(s, WeiboResult.class);
                List<WeiboResult.WeiboInfo> weiboResultList = weiboResult.getList();
                if(weiboResultList != null && !weiboResultList.isEmpty()){
                    mWeiboList.addAll(weiboResultList);
                    listener.onDataFinish(mWeiboList);
                }
                if(!weiboResult.hasNextPage() || weiboResultList == null || weiboResultList.isEmpty()){
                    listener.noMoreData();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                currentRequestIndex--;
                ToastUtil.showShort(EsGlobal.getGlobalContext(), volleyError.toString());
                listener.onError(volleyError.toString());
            }
        });
    }

    @Override
    public void reloadInfo() {
        mHasLogin = EsUserManager.getInstance().hasLogIn();
        mProfile = EsUserManager.getInstance().getUserProfile();
        currentRequestIndex = 0;
        mWeiboList.clear();
    }

    @Override
    public EsUserProfile getProfile() {
        return mProfile;
    }

    @Override
    public boolean isLogin() {
        return mHasLogin;
    }

    @Override
    public void setLocation(BDLocation bdLocation) {
        if(bdLocation != null && bdLocation.getLocType() != BDLocation.TypeServerError){
            mBDLocation = bdLocation;
            parseLocation();
        }

    }

    private void parseLocation() {
        if(mBDLocation != null && mBDLocation.getLocType() != BDLocation.TypeServerError){
            longtitude = mBDLocation.getLongitude()+"";
            latitude = mBDLocation.getLatitude()+"";
        }
    }


}
