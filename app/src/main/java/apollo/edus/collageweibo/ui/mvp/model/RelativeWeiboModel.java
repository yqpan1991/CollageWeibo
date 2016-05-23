package apollo.edus.collageweibo.ui.mvp.model;

import android.content.Context;

import java.util.List;

import apollo.edus.collageweibo.biz.bean.WeiboResult;

/**
 * Created by panyongqiang on 16/5/23.
 */
public interface RelativeWeiboModel {
    interface OnStatusListFinishedListener {
        void noMoreData();

        void onDataFinish(List<WeiboResult.WeiboInfo> statuslist);

        void onError(String error);
    }

    public void userTimeline(String uid,int type, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void userTimelineNextPage(String uid,int type, Context context, OnStatusListFinishedListener onStatusFinishedListener);

}
