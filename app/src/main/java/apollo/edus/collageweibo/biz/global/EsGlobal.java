package apollo.edus.collageweibo.biz.global;

import android.content.Context;

/**
 * Created by Panda on 2016/3/6.
 */
public class EsGlobal {

    private static Context mGlobalContext;

    private EsGlobal(){

    }

    public static void setGlobalContext(Context context){
        mGlobalContext = context;
    }

    public static Context getGlobalContext(){
        return mGlobalContext;
    }
}
