package apollo.edus.collageweibo.biz.user;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import apollo.edus.collageweibo.biz.global.EsGlobal;
import apollo.edus.collageweibo.utils.EsPreferencesUtils;

/**
 * Created by Panda on 2016/3/6.
 */
public class EsUserManager {

    private static final String SETTING_NATIVE_USERINFO = null;
    private static final String KEY_NATIVE_USERINFO = "native_userinfo";

    public static int USER_TYPE_NORMAL = 0;
    public static int USER_TYPE_ADMIN = 1;

    private static EsUserManager instance;
    private EsUser mUserInfo;

    private List<OnUserLogOperationListener> mOnUserLogOperationList;

    public static EsUserManager getInstance(){
        if(instance == null){
            synchronized (EsUserManager.class){
                if(instance == null){
                    instance = new EsUserManager();
                }
            }
        }
        return instance;
    }

    private EsUserManager(){
        mOnUserLogOperationList = new ArrayList<>();
    }

    //用户信息
    //用户是否登录了
    private boolean hasLogin;

    public void setHasLogin(boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    public boolean hasLogIn() {
        return hasLogin;
    }


    public EsUser getUserInfo() {
        return mUserInfo;
    }



    public void setUserInfo(EsUser userInfo){
        mUserInfo = userInfo;
    }

    public void saveUserInfoToNative(){
        EsPreferencesUtils.putString(EsGlobal.getGlobalContext(), KEY_NATIVE_USERINFO, new Gson().toJson(mUserInfo));
    }


    public EsUser getNativeSaveUserInfo() {
        return new Gson().fromJson(EsPreferencesUtils.getString(EsGlobal.getGlobalContext(), KEY_NATIVE_USERINFO, null), EsUser.class);
    }

    public void clearNativeSaveUserInfo() {
        EsPreferencesUtils.putString(EsGlobal.getGlobalContext(), KEY_NATIVE_USERINFO, null);
    }

    private void stop(){
        mOnUserLogOperationList.clear();
    }

    public static synchronized void destory(){
        if(instance != null){
            instance.stop();
            instance = null;
        }
    }

    public void registerOnUserLogOperationListener(OnUserLogOperationListener listener){
        if(!mOnUserLogOperationList.contains(listener)){
            mOnUserLogOperationList.add(listener);
        }
    }

    public void unregisterOnUserLogOperationListener(OnUserLogOperationListener listener){
        mOnUserLogOperationList.remove(listener);
    }

    public void notifyUserLogout(){
        for(OnUserLogOperationListener listener : mOnUserLogOperationList){
            listener.onUserLout();
        }
    }

    public void notifyUserLogin(){
        for(OnUserLogOperationListener listener : mOnUserLogOperationList){
            listener.onUserLogin();
        }
    }

    public interface OnUserLogOperationListener{
        public void onUserLogin();
        public void onUserLout();
    }

}
