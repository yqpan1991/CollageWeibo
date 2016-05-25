package apollo.edus.collageweibo.ui.mvp.impl;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;

import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.ui.mvp.model.UserModel;
import apollo.edus.collageweibo.ui.mvp.present.MyWeiBoActivityPresent;
import apollo.edus.collageweibo.ui.mvp.view.MyWeiBoActivityView;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class MyWeiBoActivityPresentImp implements MyWeiBoActivityPresent {
    private UserModel mUserModel;
    private MyWeiBoActivityView mMyWeiBoActivityView;

    public MyWeiBoActivityPresentImp(MyWeiBoActivityView myWeiBoActivityView) {
        this.mMyWeiBoActivityView = myWeiBoActivityView;
        this.mUserModel = new UserModelImp();
    }

    @Override
    public void pullToRefreshData(String uid, Context context) {
        mMyWeiBoActivityView.showLoadingIcon();
        mUserModel.userTimeline(uid, context, new UserModel.OnStatusListFinishedListener() {
            @Override
            public void noMoreData() {
                mMyWeiBoActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(List<WeiboResult.WeiboInfo> list) {
                mMyWeiBoActivityView.hideLoadingIcon();
                mMyWeiBoActivityView.updateListView(list);
            }

            @Override
            public void onError(String error) {
                mMyWeiBoActivityView.hideLoadingIcon();
                mMyWeiBoActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData(String uid, Context context) {
        mUserModel.userTimelineNextPage(uid, context, new UserModel.OnStatusListFinishedListener() {
            @Override
            public void noMoreData() {
                mMyWeiBoActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(List<WeiboResult.WeiboInfo> list) {
                mMyWeiBoActivityView.hideFooterView();
                mMyWeiBoActivityView.updateListView(list);
            }

            @Override
            public void onError(String error) {
                mMyWeiBoActivityView.showErrorFooterView();
            }
        });
    }
}
