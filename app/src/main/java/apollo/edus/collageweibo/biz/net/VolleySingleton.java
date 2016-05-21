package apollo.edus.collageweibo.biz.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import apollo.edus.collageweibo.biz.global.EsGlobal;

/**
 * Created by Panda on 2015/10/16.
 */
public class VolleySingleton {

    private static VolleySingleton mInstance;

    private Context mContext;
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context){
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    private static VolleySingleton getInstance(){
        if(mInstance == null){
            synchronized (VolleySingleton.class){
                if(mInstance == null){
                    mInstance = new VolleySingleton(EsGlobal.getGlobalContext());
                }
            }
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public static void addRequest(Request request){
        getInstance().getRequestQueue().add(request);
    }


}
