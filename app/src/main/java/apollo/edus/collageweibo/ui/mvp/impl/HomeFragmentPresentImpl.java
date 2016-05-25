package apollo.edus.collageweibo.ui.mvp.impl;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.baidu.location.service.LocationService;

import java.util.List;

import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.ui.application.MyApplication;
import apollo.edus.collageweibo.ui.mvp.model.HomeModel;
import apollo.edus.collageweibo.ui.mvp.present.HomeFragmentPresent;
import apollo.edus.collageweibo.ui.mvp.view.HomeFragmentView;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class  HomeFragmentPresentImpl implements HomeFragmentPresent, EsUserManager.OnUserLogOperationListener {

    private final String TAG = this.getClass().getSimpleName();

    private HomeFragmentView mView;
    private Activity mContext;
    private HomeModel mHomeModel;
    private LocationService locationService;


    public HomeFragmentPresentImpl(HomeFragmentView view, Activity context){
        mView = view;
        mContext = context;
        mHomeModel = new HomeModelImpl();
    }

    @Override
    public void pullToRefreshData() {
        mHomeModel.homeFirstWeiboList(new HomeModel.OnStatusListFinishedListener(){

            @Override
            public void noMoreData() {
                mView.hideLoadingIcon();
                mView.showEndFooterView();
            }

            @Override
            public void onDataFinish(List<WeiboResult.WeiboInfo> statuslist) {
                mView.hideLoadingIcon();
                mView.updateListView(statuslist);
            }

            @Override
            public void onError(String error) {
                mView.hideLoadingIcon();
                mView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData( ) {
        mHomeModel.homeNextWeiboList(new HomeModel.OnStatusListFinishedListener(){

            @Override
            public void noMoreData() {
                mView.showEndFooterView();
            }

            @Override
            public void onDataFinish(List<WeiboResult.WeiboInfo> statuslist) {
                mView.hideFooterView();
                mView.updateListView(statuslist);
            }

            @Override
            public void onError(String error) {
                mView.showErrorFooterView();
            }
        });
    }

    @Override
    public void start() {
        EsUserManager.getInstance().registerOnUserLogOperationListener(this);
        locationService = ((MyApplication) mContext.getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
        reloadData();
    }

    @Override
    public void stop() {
        locationService.stop();
        EsUserManager.getInstance().unregisterOnUserLogOperationListener(this);
    }


    @Override
    public void onUserLogin() {
        cancelRequest();
        clearCurrentData();
        reloadData();
    }

    private void reloadData() {
        mHomeModel.reloadInfo();
        mHomeModel.updateUserLocation(null, null);
        mView.showUserInfo(mHomeModel.getProfile());
        mView.showLoadingIcon();
        pullToRefreshData();
    }

    private void cancelRequest() {

    }

    @Override
    public void onUserLogout() {
        cancelRequest();
        clearCurrentData();
        reloadData();
    }

    private void clearCurrentData() {
        //TODO ---clear model data
        //show origin ui
        mView.hideLoadingIcon();
        mView.hideFooterView();
    }

    /*****
     * @see copy funtion to you project
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            Log.e(TAG,"onReceiveLocation:"+location.toString());
            mHomeModel.setLocation(location);
            mHomeModel.updateUserLocation(new Response.Listener<String>() {
                @Override
                public void onResponse(String string) {
//                    Log.e(TAG,"updateUserLocation:result:"+string);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
//                    Log.e(TAG,"updateUserLocation:error:"+volleyError.toString());
                }
            });
           /* if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                *//**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 *//*
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                Log.e(TAG,"location:"+sb.toString());
            }*/
        }

    };
}
