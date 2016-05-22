package apollo.edus.collageweibo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.ui.activity.LoginActivity;
import apollo.edus.collageweibo.ui.activity.RegisterActivity;
import apollo.edus.collageweibo.ui.activity.SettingActivity;

/**
 * Created by panyongqiang on 16/5/20.
 */
public class ProfileFragmentOfficial extends EsBaseFragment implements EsUserManager.OnUserLogOperationListener {

    private final String TAG = this.getClass().getSimpleName();

    private LinearLayout mLlUnLogin;
    private TextView mTvRegister;
    private TextView mTvLogin;

    private LinearLayout mLlLogedIn;
    private ImageView mIvAvator;
    private TextView mTvUserName;
    private TextView mTvDesc;
    private LinearLayout mLlWebibo;
    private TextView mTvWeiboCount;
    private LinearLayout mLlFriendsCount;
    private TextView mTvFriendCount;
    private LinearLayout mLlFollowers;
    private TextView mTvFollowersCount;
    private RelativeLayout mRlMyProfile;
    private RelativeLayout mRlNewFriend;
    private RelativeLayout mRlSettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_layout_official, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkLoadData();
        EsUserManager.getInstance().registerOnUserLogOperationListener(this);
    }

    private void checkLoadData() {
        //check show loginView ,unloginView
        //clear login data
        //load login data
        if(EsUserManager.getInstance().hasLogIn()){
            showUnloginView(false);
            showLogedInView(true);
            clearLoginData();
            loadLoginData();
        }else{
            showUnloginView(true);
            showLogedInView(false);
        }
    }

    private void loadLoginData() {

    }

    private void clearLoginData() {
        //clear view render data
    }

    private void showLogedInView(boolean show) {
        mLlLogedIn.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showUnloginView(boolean show) {
        mLlUnLogin.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void initView(View rootView) {
        initLogedInView(rootView);
        initUnLoginView(rootView);
    }

    private void initUnLoginView(View rootView) {
        mLlUnLogin = (LinearLayout) rootView.findViewById(R.id.ll_unlogin);
        mTvRegister = (TextView) rootView.findViewById(R.id.register);
        mTvLogin = (TextView) rootView.findViewById(R.id.login);
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });
        mTvLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

    }

    private void initLogedInView(View rootView) {
        mLlLogedIn = (LinearLayout) rootView.findViewById(R.id.ll_login);
        mIvAvator = (ImageView) rootView.findViewById(R.id.profile_myimg);
        mTvUserName = (TextView) rootView.findViewById(R.id.profile_myname);
        mTvDesc = (TextView) rootView.findViewById(R.id.profile_mydescribe);
        mLlWebibo = (LinearLayout) rootView.findViewById(R.id.yyweibo_layout);
        mTvWeiboCount = (TextView) rootView.findViewById(R.id.profile_statuses_count);
        mLlFriendsCount = (LinearLayout) rootView.findViewById(R.id.friends_layout);
        mTvFriendCount = (TextView) rootView.findViewById(R.id.profile_friends_count);
        mLlFollowers = (LinearLayout) rootView.findViewById(R.id.followers_layout);
        mTvFollowersCount = (TextView) rootView.findViewById(R.id.profile_followers_count);
        mRlMyProfile = (RelativeLayout) rootView.findViewById(R.id.rl_my_favorite);
        mRlNewFriend = (RelativeLayout) rootView.findViewById(R.id.rl_new_friend);
        mRlSettings = (RelativeLayout) rootView.findViewById(R.id.rl_settings);
        mRlSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
    }

    @Override
    public void onUserLogin() {
        checkLoadData();
    }

    @Override
    public void onUserLout() {
        checkLoadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EsUserManager.getInstance().unregisterOnUserLogOperationListener(this);
    }
}
