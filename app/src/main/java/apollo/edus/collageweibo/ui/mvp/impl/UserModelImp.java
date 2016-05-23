package apollo.edus.collageweibo.ui.mvp.impl;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.biz.user.EsUser;
import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.ui.mvp.model.UserModel;
import apollo.edus.collageweibo.utils.ToastUtil;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class UserModelImp implements UserModel {
    private ArrayList<WeiboResult.WeiboInfo> mStatusList = new ArrayList<>();
    private ArrayList<EsUserProfile> mFollowersList = new ArrayList<>();
    private ArrayList<EsUserProfile> mFriendsList = new ArrayList<>();
    private int mFollowersCursor;
    private int mFriendsCursor;
    private ArrayList<EsUserProfile> mUserArrayList;
    private int currentPageIndex;

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
    public void userTimelineNextPage(final String uid, final Context context, final OnStatusListFinishedListener onStatusFinishedListener) {
        currentPageIndex ++;
        EsApiHelper.fetchUserWeiboList(uid, currentPageIndex, new Response.Listener<String>() {
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

    @Override
    public void followers(final String uid, final Context context, final OnUserListRequestFinish onUserListRequestFinish) {
/*
        FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));

        mFriendshipsAPI.followers(uid, 30, 0, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<User> temp = UserList.parse(response).usersList;
                if (temp != null && temp.size() > 0) {
                    if (mFollowersList != null) {
                        mFollowersList.clear();
                    }
                    mFollowersList = temp;
                    mFollowersCursor = Integer.valueOf(StatusList.parse(response).next_cursor);
                    onUserListRequestFinish.onDataFinish(mFollowersList);
                } else {
                    ToastUtil.showShort(context, "没有更新的内容了");
                    onUserListRequestFinish.noMoreData();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onUserListRequestFinish.onError(e.getMessage());
            }
        });*/
    }

    @Override
    public void followersNextPage(final String uid, final Context context, final OnUserListRequestFinish onUserListRequestFinish) {
        /*FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mFriendshipsAPI.followers(uid, 20, mFollowersCursor, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<User> temp = UserList.parse(response).usersList;
                    if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(mFollowersList.get(mFollowersList.size() - 1).id))) {
                        onUserListRequestFinish.noMoreData();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mFollowersList.addAll(temp);
                        mFollowersCursor = Integer.valueOf(UserList.parse(response).next_cursor);
                        onUserListRequestFinish.onDataFinish(mFollowersList);
                    }
                } else {
                    ToastUtil.showShort(context, "内容已经加载完了");
                    onUserListRequestFinish.noMoreData();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onUserListRequestFinish.onError(e.getMessage());
            }
        });*/
    }

    @Override
    public void friends(final String uid, final Context context, final OnUserListRequestFinish onUserListRequestFinish) {
       /* FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));

        mFriendshipsAPI.friends(uid, 30, 0, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<User> temp = UserList.parse(response).usersList;

                if (temp != null && temp.size() > 0) {
                    if (mFriendsList != null) {
                        mFriendsList.clear();
                    }
                    mFriendsList = temp;
                    mFriendsCursor = Integer.valueOf(StatusList.parse(response).next_cursor);
                    onUserListRequestFinish.onDataFinish(mFriendsList);
                } else {
                    ToastUtil.showShort(context, "没有更新的内容了");
                    onUserListRequestFinish.noMoreData();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onUserListRequestFinish.onError(e.getMessage());
            }
        });*/
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
        /*FriendshipsAPI mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mFriendshipsAPI.friends(uid, 20, mFriendsCursor, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<User> temp = UserList.parse(response).usersList;
                    if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(mFriendsList.get(mFriendsList.size() - 1).id))) {
                        onUserListRequestFinish.noMoreData();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mFriendsList.addAll(temp);
                        mFriendsCursor = Integer.valueOf(UserList.parse(response).next_cursor);
                        onUserListRequestFinish.onDataFinish(mFriendsList);
                    }
                } else {
                    ToastUtil.showShort(context, "内容已经加载完了");
                    onUserListRequestFinish.noMoreData();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onUserListRequestFinish.onError(e.getMessage());
            }
        });*/
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
