package apollo.edus.collageweibo.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.ui.activity.LoginActivity;
import apollo.edus.collageweibo.ui.activity.MyProfileDetailActivity;
import apollo.edus.collageweibo.ui.activity.MyWeiboActivity;
import apollo.edus.collageweibo.ui.activity.RegisterActivity;
import apollo.edus.collageweibo.ui.activity.SettingActivity;

/**
 * Created by panyongqiang on 16/5/20.
 */
public class ProfileFragmentOfficial extends EsBaseFragment implements EsUserManager.OnUserLogOperationListener {
    private static final int REQUEST_CODE_UPDATE_PROFILE = 1000;

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
    private RelativeLayout mRlMyFavorite;

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
            clearLogedInData();
            loadLogedInData();
        }else{
            showUnloginView(true);
            showLogedInView(false);
        }
    }

    private void loadLogedInData() {
        EsUserProfile userProfile = EsUserManager.getInstance().getUserProfile();
        if(userProfile != null){
            mTvWeiboCount.setText(userProfile.getWeibo()+"");
            mTvFriendCount.setText(userProfile.getFans()+"");
            mTvFollowersCount.setText(userProfile.getAttention()+"");
            mTvUserName.setText(userProfile.getNickName());
            if(TextUtils.isEmpty(userProfile.getUserSgin())){
                mTvDesc.setText("用户很懒,没有签名");
            }else{
                mTvDesc.setText(userProfile.getUserSgin());
            }
        }
    }

    private void clearLogedInData() {
        mTvWeiboCount.setText("0");
        mTvFriendCount.setText("0");
        mTvFollowersCount.setText("0");
        mTvUserName.setText("");
        mTvDesc.setText("用户很懒,没有签名");
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
        mRlMyProfile = (RelativeLayout) rootView.findViewById(R.id.rl_my_profile);
        mRlMyFavorite = (RelativeLayout) rootView.findViewById(R.id.rl_my_favorite);
        mRlNewFriend = (RelativeLayout) rootView.findViewById(R.id.rl_new_friend);
        mRlSettings = (RelativeLayout) rootView.findViewById(R.id.rl_settings);

        mLlWebibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStartWeiboActivity();
            }
        });
        mRlMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), MyProfileDetailActivity.class), REQUEST_CODE_UPDATE_PROFILE);
            }
        });
        mRlMyFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRlNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRlSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
    }

    private void checkStartWeiboActivity() {
        startActivity(new Intent(getActivity(), MyWeiboActivity.class));
/*        String userID = EsUserManager.getInstance().getUserInfo().getUserId();
        EsApiHelper.fetchUserWeiboList(userID, 2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e(TAG,"suc:"+s);
                Gson gson = new Gson();
                WeiboResult weiboResult = gson.fromJson(s, WeiboResult.class);
                Log.e(TAG,gson.toJson(weiboResult).toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), volleyError.toString(),Toast.LENGTH_SHORT).show();
            }
        });*/
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CODE_UPDATE_PROFILE){
                loadLogedInData();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EsUserManager.getInstance().unregisterOnUserLogOperationListener(this);
    }


}
