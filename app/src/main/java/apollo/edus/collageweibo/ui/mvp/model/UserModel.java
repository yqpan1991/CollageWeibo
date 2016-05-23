package apollo.edus.collageweibo.ui.mvp.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.user.EsUserProfile;

/**
 * Created by wenmingvs on 16/5/14.
 */
public interface UserModel {

    interface OnUserDetailRequestFinish {
        void onComplete(EsUserProfile user);

        void onError(String error);
    }


    interface OnUserListRequestFinish {
        void noMoreDate();

        void onDataFinish(ArrayList<EsUserProfile> userlist);

        void onError(String error);
    }

    interface OnStatusListFinishedListener {
        void noMoreData();

        void onDataFinish(List<WeiboResult.WeiboInfo> statuslist);

        void onError(String error);
    }

    interface OnUserDeleteListener {
        void onSuccess(ArrayList<EsUserProfile> userlist);

        void onEmpty();

        void onError(String error);
    }


    public void showUserDetail(String uid, Context context, OnUserDetailRequestFinish onUserDetailRequestFinish);

    public EsUserProfile showUserDetailSync(String uid, Context context);

    public void userTimeline(String uid, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void userTimelineNextPage(String uid, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void followers(String uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void followersNextPage(String uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void friends(String uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void friendsNextPage(String uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void getUserDetailList(Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void deleteUserByUid(String uid, Context context, OnUserDeleteListener onUserDeleteListener);
}


