package apollo.edus.collageweibo.ui.mvp.impl;

import android.content.Context;

import java.util.List;

import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.ui.mvp.model.RelativeWeiboModel;
import apollo.edus.collageweibo.ui.mvp.model.UserModel;
import apollo.edus.collageweibo.ui.mvp.present.MyWeiBoActivityPresent;
import apollo.edus.collageweibo.ui.mvp.present.RelativeWeiboListActivityPresent;
import apollo.edus.collageweibo.ui.mvp.view.MyWeiBoActivityView;
import apollo.edus.collageweibo.ui.mvp.view.RelativeWeiboListActivityView;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class RelativeWeiboListActivityPresentImp implements RelativeWeiboListActivityPresent {
    private RelativeWeiboModel mModel;
    private RelativeWeiboListActivityView mMyWeiBoActivityView;
    private int type;

    public RelativeWeiboListActivityPresentImp(RelativeWeiboListActivityView myWeiBoActivityView, int type) {
        this.mMyWeiBoActivityView = myWeiBoActivityView;
        this.mModel = new RelativeWeiboModelImpl();
        this.type = type;

    }


    @Override
    public void pullToRefreshData(String uid, Context context) {
        mMyWeiBoActivityView.showLoadingIcon();
        mModel.userTimeline(uid,type, context, new RelativeWeiboModel.OnStatusListFinishedListener() {
            @Override
            public void noMoreData() {
                mMyWeiBoActivityView.hideLoadingIcon();
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
        mModel.userTimelineNextPage(uid, type, context, new RelativeWeiboModel.OnStatusListFinishedListener() {
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
