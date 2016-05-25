package apollo.edus.collageweibo.biz.user;

import java.util.ArrayList;
import java.util.List;

/**
 * 目前只是做简单的逻辑处理
 * 不用于上线
 * Created by panyongqiang on 16/5/23.
 */
public class EsFavoriteTempManager implements EsUserManager.OnUserLogOperationListener {
    private static EsFavoriteTempManager instance;
    private List<String> favoriteWeiboList;

    public static EsFavoriteTempManager getInstance(){
        if(instance == null){
            synchronized (EsFavoriteTempManager.class){
                if(instance == null){
                    instance = new EsFavoriteTempManager();
                }
            }
        }
        return instance;
    }

    private EsFavoriteTempManager(){
        favoriteWeiboList = new ArrayList<>();
        EsUserManager.getInstance().registerOnUserLogOperationListener(this);
    }

    private void stop(){
        EsUserManager.getInstance().unregisterOnUserLogOperationListener(this);
    }

    public void addFavorite(String weiboId){
        favoriteWeiboList.add(weiboId);
    }

    public void delFavorite(String weiboId){
        favoriteWeiboList.remove(weiboId);
    }

    public List<String> getFavoriteWeiboListSnapShot(){
        return new ArrayList<>(favoriteWeiboList);
    }

    public static synchronized void destroy(){
        if(instance != null){
            instance.stop();
        }
    }


    @Override
    public void onUserLogin() {
        favoriteWeiboList.clear();
    }

    @Override
    public void onUserLogout() {
        favoriteWeiboList.clear();
    }
}
