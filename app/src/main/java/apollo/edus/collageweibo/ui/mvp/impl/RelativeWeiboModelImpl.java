package apollo.edus.collageweibo.ui.mvp.impl;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.ui.mvp.model.RelativeWeiboModel;
import apollo.edus.collageweibo.utils.ToastUtil;

/**
 * Created by panyongqiang on 16/5/23.
 */
public class RelativeWeiboModelImpl implements RelativeWeiboModel {

    private ArrayList<WeiboResult.WeiboInfo> mStatusList = new ArrayList<>();
    private int currentPageIndex;

    @Override
    public void userTimeline(String uid, int type, final Context context, final OnStatusListFinishedListener onStatusFinishedListener) {
        EsApiHelper.getRelativeWeibo(uid, type, 0, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //reset index = 0;
                //clear result
                //add to result
                //check no more data
                //check result
                currentPageIndex = 0;
                mStatusList.clear();
                Gson gson = new Gson();
                WeiboResult weiboResult = gson.fromJson(s, WeiboResult.class);
                List<WeiboResult.WeiboInfo> weiboResultList = weiboResult.getList();
                if(!weiboResult.hasNextPage() || weiboResultList == null || weiboResultList.isEmpty()){
                    onStatusFinishedListener.noMoreData();
                }
                if(weiboResultList != null && !weiboResultList.isEmpty()){
                    mStatusList.addAll(weiboResultList);
                    onStatusFinishedListener.onDataFinish(mStatusList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showShort(context, volleyError.toString());
                onStatusFinishedListener.onError(volleyError.toString());
            }
        });
    }

    @Override
    public void userTimelineNextPage(final String uid, final int type, final Context context, final OnStatusListFinishedListener onStatusFinishedListener) {
        currentPageIndex ++;
        EsApiHelper.getRelativeWeibo(uid, type, currentPageIndex, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //reset index ++;
                //add to result
                //check no more data
                //check result
                Gson gson = new Gson();
                WeiboResult weiboResult = gson.fromJson(s, WeiboResult.class);
                List<WeiboResult.WeiboInfo> weiboResultList = weiboResult.getList();
                if(!weiboResult.hasNextPage() || weiboResultList == null || weiboResultList.isEmpty()){
                    onStatusFinishedListener.noMoreData();
                }
                if(weiboResultList != null && !weiboResultList.isEmpty()){
                    mStatusList.addAll(weiboResultList);
                    onStatusFinishedListener.onDataFinish(mStatusList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(currentPageIndex >= 0){
                    currentPageIndex --;
                }
                ToastUtil.showShort(context, volleyError.toString());
                onStatusFinishedListener.onError(volleyError.toString());
            }
        });
    }
}
