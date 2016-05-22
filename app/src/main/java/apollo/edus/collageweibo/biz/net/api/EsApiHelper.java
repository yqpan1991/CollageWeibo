package apollo.edus.collageweibo.biz.net.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import apollo.edus.collageweibo.biz.net.VolleySingleton;
import apollo.edus.collageweibo.biz.net.request.CustomStringRequest;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.utils.EsMd5Util;

/**
 * Created by Panda on 2016/5/21.
 */
public class EsApiHelper {
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

    public static void registerUser(final String userName, final String password, final String nickName,Response.Listener<String> listener, Response.ErrorListener errorListener) {
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

    public static void userLogout(){

    }

    //---------------------------用户发文字微博
    public static void shareContentWeibo(final String contents, Response.Listener<String> sucListener, Response.ErrorListener errorListener){
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

    public static void shareImageWeibo(){

    }

    public static void retweetWebibo(String contents, String weiboId, String forwardingUserId, Response.Listener<String> sucListener, Response.ErrorListener errorListener){

    }

    public static void deleteWeibo(final String weiboId, Response.Listener<String> sucListener, Response.ErrorListener errorListener){
        final String userId = EsUserManager.getInstance().getUserInfo().getUserId();
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "901");
                hashMap.put(EsApiKeys.KEY_USERID, userId);
                hashMap.put(EsApiKeys.KEY_WEIBO_ID, weiboId);
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);

    }

    public static void searchUser(final String keywords, final int pageSize, Response.Listener<String> sucListener, Response.ErrorListener errorListener){
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "900");
                hashMap.put(EsApiKeys.KEY_KEYWORDS, keywords);
                hashMap.put(EsApiKeys.KEY_PAGESIZE, pageSize+"");
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }

    public static void fetchUserWeiboList(final String userId, final int pageSize, Response.Listener<String> sucListener, Response.ErrorListener errorListener){
        CustomStringRequest stringRequest = new CustomStringRequest(Request.Method.POST, EsApi.getHost(), sucListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(EsApiKeys.KEY_PORT, "900");
                hashMap.put(EsApiKeys.KEY_USERID, userId);
                hashMap.put(EsApiKeys.KEY_PAGESIZE, pageSize+"");
                return hashMap;
            }
        };
        VolleySingleton.addRequest(stringRequest);
    }

}