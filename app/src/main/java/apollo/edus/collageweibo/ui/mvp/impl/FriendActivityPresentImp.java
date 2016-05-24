package apollo.edus.collageweibo.ui.mvp.impl;

import android.content.Context;

import java.util.ArrayList;

import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.ui.mvp.model.UserModel;
import apollo.edus.collageweibo.ui.mvp.present.FriendActivityPresent;
import apollo.edus.collageweibo.ui.mvp.view.FriendActivityView;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class FriendActivityPresentImp implements FriendActivityPresent {

    private UserModel mUserModel;
    private FriendActivityView mFriendActivityView;

    public FriendActivityPresentImp(FriendActivityView friendActivityView) {
        this.mFriendActivityView = friendActivityView;
        this.mUserModel = new UserModelImp();
    }

    @Override
    public void pullToRefreshData(String uid, Context context) {
        mFriendActivityView.showLoadingIcon();
        mUserModel.friends(uid, context, new UserModel.OnUserListRequestFinish() {
            @Override
            public void noMoreData() {
                mFriendActivityView.hideLoadingIcon();

            }

            @Override
            public void onDataFinish(ArrayList<EsUserProfile> userlist) {
                mFriendActivityView.hideLoadingIcon();
                mFriendActivityView.updateListView(userlist);
            }

            @Override
            public void onError(String error) {
                mFriendActivityView.hideLoadingIcon();
                mFriendActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData(String uid, Context context) {
        mUserModel.friendsNextPage(uid, context, new UserModel.OnUserListRequestFinish() {
            @Override
            public void noMoreData() {
                mFriendActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(ArrayList<EsUserProfile> userlist) {
                mFriendActivityView.hideFooterView();
                mFriendActivityView.updateListView(userlist);
            }

            @Override
            public void onError(String error) {
                mFriendActivityView.showErrorFooterView();
            }
        });
    }
}
