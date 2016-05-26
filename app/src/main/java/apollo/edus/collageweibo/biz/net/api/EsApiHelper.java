package apollo.edus.collageweibo.biz.net.api;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import apollo.edus.collageweibo.biz.bean.ImageInfo;
import apollo.edus.collageweibo.biz.global.EsGlobal;
import apollo.edus.collageweibo.biz.net.VolleySingleton;
import apollo.edus.collageweibo.biz.net.request.CustomStringRequest;
import apollo.edus.collageweibo.biz.threadpool.ThreadPoolManager;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.ui.popupwindow.EsThumbnailUtils;
import apollo.edus.collageweibo.utils.EsFileUtils;
import apollo.edus.collageweibo.utils.EsImageUtil;
import apollo.edus.collageweibo.utils.EsMd5Util;
import apollo.edus.collageweibo.utils.ScreenUtil;

/**
 * Created by Panda on 2016/5/21.
 */
public class EsApiHelper {
    private final static String TAG = EsApiHelper.class.getSimpleName();

    private EsApiHelper() {

    }

    public static void userLogin(final String userName, final String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "1000");
                hashMap.put(EsApiKeys.KEY_USERNAME, userName);
                hashMap.put(EsApiKeys.KEY_PASSOWORD, EsMd5Util.toMd5(password));
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }

    public static void registerUser(final String userName, final String password, final String nickName, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "1001");
                hashMap.put(EsApiKeys.KEY_USERNAME, userName);
                hashMap.put(EsApiKeys.KEY_PASSOWORD, EsMd5Util.toMd5(password));
                hashMap.put(EsApiKeys.KEY_NICKNAME, nickName);
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }

    public static void uploadUserAvator() {

    }

    public static void fetchUserInfo() {

    }

    public static void alterPassword() {

    }


    public static void alterUserProfile() {

    }

    public static void userLogout() {

    }

    //---------------------------用户发文字微博
    public static void shareContentWeibo(final String contents, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        final String userId = EsUserManager.getInstance().getUserInfo().getUserId();
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "901");
                hashMap.put(EsApiKeys.KEY_USERID, userId);
                hashMap.put(EsApiKeys.KEY_CONTNETS, contents);
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }


    public static void retweetWebibo(final String userId, final String contents, final String weiboId, final String forwardingUserId, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "902");
                hashMap.put(EsApiKeys.KEY_USERID, userId);
                hashMap.put(EsApiKeys.KEY_CONTNETS, contents);
                hashMap.put(EsApiKeys.KEY_FORWARDING_USERID, forwardingUserId);
                hashMap.put(EsApiKeys.KEY_WEIBO_ID, weiboId);
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }

    public static void shareImageWeibo(final String contents, final List<ImageInfo> imageList, final Response.Listener<String> sucListener, final Response.ErrorListener errorListener) {
        if (imageList == null || imageList.isEmpty()) {
            shareContentWeibo(contents, sucListener, errorListener);
            return;
        }
        final String userId = EsUserManager.getInstance().getUserInfo().getUserId();
        ThreadPoolManager.getInstance().getPreUploadThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                uploadWeiboAndImage(userId, contents, new ArrayList<>(imageList), sucListener, errorListener);
            }
        });

        //1. upload weibo content
        //2. image 2 base64
        //3. upload image
        //4. return is ok
    }

    private static void uploadWeiboAndImage(final String userId, final String contents, List<ImageInfo> imageList, final Response.Listener<String> sucListener, final Response.ErrorListener errorListener) {
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, EsApi.getHost(), requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "901");
                hashMap.put(EsApiKeys.KEY_USERID, userId);
                hashMap.put(EsApiKeys.KEY_CONTNETS, contents);
                return hashMap;
            }
        };
        VolleySingleton.addRequest(request);
        try {
            final String result = requestFuture.get(15, TimeUnit.SECONDS);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (EsApiResultHelper.isSuc(jsonObject)) {
                    ImageInfo info = imageList.get(0);
                    int width = ScreenUtil.dip2px(EsGlobal.getGlobalContext(), 100);
                    Bitmap bitmap = EsThumbnailUtils.createScaleImageThumbnail(info.getImageFile().getAbsolutePath(), width, width, false);
                    String base64 = EsImageUtil.bmpToB64(bitmap);
                    String weiboId = jsonObject.optString(EsApiKeys.KEY_WEIBO_ID, "");
                    Log.e(TAG, "bitmap to base64:" + base64);
                    uploadWeiboData(weiboId, base64, userId, EsFileUtils.getFileExtensionAndDot(info.getImageFile().getAbsolutePath()), sucListener, errorListener);
                } else {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                errorListener.onErrorResponse(new VolleyError(EsApiResultHelper.getErrorString(new JSONObject(result))));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (final JSONException e) {
                e.printStackTrace();
                if (errorListener != null) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            errorListener.onErrorResponse(new VolleyError(e.toString()));
                        }
                    });
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void uploadWeiboData(final String weiboId, final String base64, final String userId, final String suffix, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "905");
                hashMap.put(EsApiKeys.KEY_WEIBO_ID, weiboId);
                hashMap.put(EsApiKeys.KEY_USERID, userId);
                hashMap.put(EsApiKeys.KEY_DATA, base64);
                hashMap.put(EsApiKeys.KEY_SUFFIX, suffix);
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }


    public static void deleteWeibo(final String weiboId, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        final String userId = EsUserManager.getInstance().getUserInfo().getUserId();
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "904");
                hashMap.put(EsApiKeys.KEY_USERID, userId);
                hashMap.put(EsApiKeys.KEY_WEIBO_ID, weiboId);
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);

    }

    public static void searchUser(final String keywords, final int pageSize, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "900");
                hashMap.put(EsApiKeys.KEY_KEYWORDS, keywords);
                hashMap.put(EsApiKeys.KEY_PAGESIZE, pageSize + "");
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }

    public static void fetchUserWeiboList(final String userId, final int pageSize, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "903");
                hashMap.put(EsApiKeys.KEY_USERID, userId);
                hashMap.put(EsApiKeys.KEY_PAGESIZE, pageSize + "");
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }

    /**
     * 评论微博
     */
    public static void commentWeibo(final String content, final String weiboId, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        final String userId = EsUserManager.getInstance().getUserInfo().getUserId();
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "906");
                hashMap.put(EsApiKeys.KEY_USERID, userId);
                hashMap.put(EsApiKeys.KEY_WEIBO_ID, weiboId);
                hashMap.put(EsApiKeys.KEY_CONTNETS, content);
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }

    /**
     * 点赞/取消点赞微博
     */
    public static void favoriteWeibo(final boolean isFavorite, final String weiboId, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        final String userId = EsUserManager.getInstance().getUserInfo().getUserId();
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "907");
                hashMap.put(EsApiKeys.KEY_USERID, userId);
                hashMap.put(EsApiKeys.KEY_WEIBO_ID, weiboId);
                hashMap.put(EsApiKeys.KEY_FAVORITE_ACTION, isFavorite ? 1 + "" : 2 + "");
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }


    public static void getWeiboDetail(final String weiboId, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "908");
                hashMap.put(EsApiKeys.KEY_WEIBO_ID, weiboId);
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }

    /**
     * 获取个人相关的微博
     *
     * @param type 1我点评的2我赞的 3@我的
     */
    public static void getRelativeWeibo(final String userId, final int type, final int pageSize, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.GET, EsApi.getFullUrl(EsApi.RELATIVE_URL, "909", userId, type + "", pageSize + ""), sucListener, errorListener);
        stringRequest.setShouldCache(false);
        VolleySingleton.addRequest(stringRequest);
    }

    /**
     * 关注/取消关注
     */
    public static void followUser(final String userId, final boolean follow, final String inUserId, final int pageSize, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        int action = follow ? 1 : 2;
        //?por=%s&userid=%s&action=%s&in_userid=%s
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.GET, EsApi.getFullUrl(EsApi.FOLLOW_URL, "100", userId, action + "", inUserId), sucListener, errorListener);
        stringRequest.setShouldCache(false);
        VolleySingleton.addRequest(stringRequest);
    }

    /**
     * 我的关注/我的粉丝用户
     */
    public static void getMyRelativeUserList(final String userId, int type, int pageSize, Response.Listener<String> sucListener, Response.ErrorListener errorListener) {
        //?por=%s&userid=%s&type=%s
        //TODO -------- 缺少pageSize
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.GET, EsApi.getFullUrl(EsApi.MY_RELATIVE_USER_URL, "101", userId, type+""), sucListener, errorListener);
        stringRequest.setShouldCache(false);
        VolleySingleton.addRequest(stringRequest);
    }

    public static void getHomePage(final String userId, final String longitude, final String latitude, int pageSize, Response.Listener<String> sucListener, Response.ErrorListener errorListener){
        String url = null;
        if(TextUtils.isEmpty(userId)){
            url = EsApi.getFullUrl(EsApi.HOME_PAGE_URL, "102", longitude, latitude, pageSize+"");
        }else{
            url = EsApi.getFullUrl(EsApi.HOME_PAGE_WITH_USER_URL, "102", userId, longitude, latitude, pageSize+"");
        }
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.GET, url, sucListener, errorListener);
        stringRequest.setShouldCache(false);
        VolleySingleton.addRequest(stringRequest);
    }

    public static void updateUserLocation(final String userId, final String longitude, final String latitude, Response.Listener<String> sucListener, Response.ErrorListener errorListener){
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.GET, EsApi.getFullUrl(EsApi.UPDATE_LOCATION_USER_URL, "103", userId, longitude, latitude ), sucListener, errorListener);
        stringRequest.setShouldCache(false);
        VolleySingleton.addRequest(stringRequest);
    }



}