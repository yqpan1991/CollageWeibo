package apollo.edus.collageweibo.biz.net.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import apollo.edus.collageweibo.biz.net.VolleySingleton;
import apollo.edus.collageweibo.biz.net.request.CustomStringRequest;
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


}