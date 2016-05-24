package apollo.edus.collageweibo.ui.mvp.impl;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import apollo.edus.collageweibo.biz.bean.UserResult;
import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.ui.mvp.model.UserModel;
import apollo.edus.collageweibo.utils.ToastUtil;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class UserModelImp implements UserModel {
    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<WeiboResult.WeiboInfo> mStatusList = new ArrayList<>();
    private ArrayList<EsUserProfile> mFollowersList = new ArrayList<>();
    private ArrayList<EsUserProfile> mFriendsList = new ArrayList<>();
    private int mFollowersCursor;
    private int mFriendsCursor;
    private ArrayList<EsUserProfile> mUserArrayList;
    private int currentTimelineIndex;
    private int currentFriendsIndex;
    private int currentFollowersIndex;

    @Override
    public void showUserDetail(String uid, final Context context, final OnUserDetailRequestFinish onUserRequestFinish) {
       /* UsersAPI mUsersAPI = new UsersAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mUsersAPI.show(uid, new RequestListener() {
            @Override
            public void onComplete(String response) {
                SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "username_" + AccessTokenKeeper.readAccessToken(context).getUid(), response);
                User user = User.parse(response);
                onUserRequestFinish.onComplete(user);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onUserRequestFinish.onError(e.getMessage());
            }
        });*/
    }

    @Override
    public EsUserProfile showUserDetailSync(String uid, final Context context) {
/*        UsersAPI mUsersAPI = new UsersAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        return User.parse(mUsersAPI.showSync(uid));*/
        return null;
    }

    @Override
    public void userTimeline(final String uid, final Context context, final OnStatusListFinishedListener onStatusFinishedListener) {
        EsApiHelper.fetchUserWeiboList(uid, 0, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //reset index = 0;
                //clear result
                //add to result
                //check no more data
                //check result
                currentTimelineIndex = 0;
                mStatusList.clear();
                Gson gson = new Gson();
                Log.e(TAG,"userTimeline result:"+s);
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
    public void userTimelineNextPage(final String uid, final Context context, final OnStatusListFinishedListener onStatusFinishedListener) {
        currentTimelineIndex++;
        EsApiHelper.fetchUserWeiboList(uid, currentTimelineIndex, new Response.Listener<String>() {
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
                if(currentTimelineIndex > 0){
                    currentTimelineIndex--;
                }
                ToastUtil.showShort(context, volleyError.toString());
                onStatusFinishedListener.onError(volleyError.toString());
            }
        });
    }

    @Override
    public void followers(final String uid, final Context context, final OnUserListRequestFinish onUserListRequestFinish) {
        EsApiHelper.getMyRelativeUserList(uid, 2, 0, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //reset index = 0;
                //clear result
                //add to result
                //check no more data
                //check result
                currentFollowersIndex = 0;
                mFollowersList.clear();
                Gson gson = new Gson();
                Log.e(TAG,"getMyRelativeUserList result:"+s);
                UserResult userResult = gson.fromJson(s, UserResult.class);
                List<EsUserProfile> userProfileList = userResult.getList();
                if(!userResult.hasNextPage() || userProfileList == null || userProfileList.isEmpty()){
                    onUserListRequestFinish.noMoreData();
                }
                if(userProfileList != null && !userProfileList.isEmpty()){
                    mFollowersList.addAll(userProfileList);
                    onUserListRequestFinish.onDataFinish(mFollowersList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showShort(context, volleyError.toString());
                onUserListRequestFinish.onError(volleyError.toString());
            }
        });
    }

    @Override
    public void followersNextPage(final String uid, final Context context, final OnUserListRequestFinish onUserListRequestFinish) {
        currentFollowersIndex ++;
        EsApiHelper.getMyRelativeUserList(uid, 2, currentFollowersIndex, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //add to result
                //check no more data
                //check result
                Gson gson = new Gson();
                Log.e(TAG,"getMyRelativeUserList result:"+s);
                UserResult userResult = gson.fromJson(s, UserResult.class);
                List<EsUserProfile> userProfileList = userResult.getList();
                if(!userResult.hasNextPage() || userProfileList == null || userProfileList.isEmpty()){
                    onUserListRequestFinish.noMoreData();
                }
                if(userProfileList != null && !userProfileList.isEmpty()){
                    mFollowersList.addAll(userProfileList);
                    onUserListRequestFinish.onDataFinish(mFollowersList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(currentFollowersIndex > 0){
                    currentFollowersIndex--;
                }
                ToastUtil.showShort(context, volleyError.toString());
                onUserListRequestFinish.onError(volleyError.toString());
            }
        });
    }

    @Override
    public void friends(final String uid, final Context context, final OnUserListRequestFinish onUserListRequestFinish) {
        EsApiHelper.getMyRelativeUserList(uid, 1,0, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //reset index = 0;
                //clear result
                //add to result
                //check no more data
                //check result
                currentFriendsIndex = 0;
                mFriendsList.clear();
                Gson gson = new Gson();
                Log.e(TAG,"getMyRelativeUserList result:"+s);
                UserResult userResult = gson.fromJson(s, UserResult.class);
                List<EsUserProfile> userProfileList = userResult.getList();
                if(!userResult.hasNextPage() || userProfileList == null || userProfileList.isEmpty()){
                    onUserListRequestFinish.noMoreData();
                }
                if(userProfileList != null && !userProfileList.isEmpty()){
                    mFriendsList.addAll(userProfileList);
                    onUserListRequestFinish.onDataFinish(mFriendsList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showShort(context, volleyError.toString());
                onUserListRequestFinish.onError(volleyError.toString());
            }
        });
    }

    /**
     * 获取指定用户的粉丝列表
     *
     * @param uid
     * @param context
     * @param onUserListRequestFinish
     */
    @Override
    public void friendsNextPage(final String uid, final Context context, final OnUserListRequestFinish onUserListRequestFinish) {
        currentFriendsIndex ++;
        EsApiHelper.getMyRelativeUserList(uid, 1, currentFriendsIndex, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //add to result
                //check no more data
                //check result
                Gson gson = new Gson();
                Log.e(TAG,"getMyRelativeUserList result:"+s);
                UserResult userResult = gson.fromJson(s, UserResult.class);
                List<EsUserProfile> userProfileList = userResult.getList();
                if(!userResult.hasNextPage() || userProfileList == null || userProfileList.isEmpty()){
                    onUserListRequestFinish.noMoreData();
                }
                if(userProfileList != null && !userProfileList.isEmpty()){
                    mFriendsList.addAll(userProfileList);
                    onUserListRequestFinish.onDataFinish(mFriendsList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(currentFriendsIndex > 0){
                    currentFriendsIndex--;
                }
                ToastUtil.showShort(context, volleyError.toString());
                onUserListRequestFinish.onError(volleyError.toString());
            }
        });
    }

    @Override
    public void getUserDetailList(final Context context, final OnUserListRequestFinish onUserListRequestFinish) {
       /* String jsonstring = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt");
        if (jsonstring == null && AccessTokenKeeper.readAccessToken(context).isSessionValid()) {
            cacheCurrentOuthToken(context);
        }
        final ArrayList<Token> tokenList = TokenList.parse(SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "登录列表缓存.txt")).tokenList;
        if (tokenList == null || tokenList.size() == 0) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mUserArrayList = new ArrayList<User>();
                for (Token token : tokenList) {
                    mUserArrayList.add(showUserDetailSync(Long.valueOf(token.getUid()), context));
                }
                onUserListRequestFinish.onDataFinish(mUserArrayList);
            }
        }).start();*/
    }

    @Override
    public void deleteUserByUid(String uid, Context context, OnUserDeleteListener onUserDeleteListener) {
        /*int i = 0;
        for (i = 0; i < mUserArrayList.size(); i++) {
            if (mUserArrayList.get(i).id.equals(String.valueOf(uid))) {
                mUserArrayList.remove(i);
                i--;
                break;
            }
        }

        if (mUserArrayList.size() == 0) {
            onUserDeleteListener.onEmpty();
            return;
        }


        if (i >= mUserArrayList.size()) {
            onUserDeleteListener.onError("没有找到对应的账户");
        } else {
            onUserDeleteListener.onSuccess(mUserArrayList);
        }*/
    }


}
