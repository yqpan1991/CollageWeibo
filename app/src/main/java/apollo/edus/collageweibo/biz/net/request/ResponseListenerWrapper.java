package apollo.edus.collageweibo.biz.net.request;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import apollo.edus.collageweibo.biz.net.api.EsApiResultHelper;


/**
 * Created by Panda on 2016/3/6.
 */
public class ResponseListenerWrapper implements Response.Listener<String>{
    public Response.Listener<String> mSucListener;
    private Response.ErrorListener mErrorListener;

    public ResponseListenerWrapper(Response.Listener<String> sucListener, Response.ErrorListener errorListener){
        mSucListener = sucListener;
        mErrorListener = errorListener;
    }

    @Override
    public void onResponse(String s) {
        try {
            if(EsApiResultHelper.isSuc(new JSONObject(s))){
                if(mSucListener != null){
                    mSucListener.onResponse(s);
                }
            }else{
                if(mErrorListener != null){
                    mErrorListener.onErrorResponse(new VolleyError(EsApiResultHelper.getErrorString(new JSONObject(s))));
                }
            }
        } catch (JSONException e) {
            if(mErrorListener != null){
                mErrorListener.onErrorResponse(new VolleyError(e.toString()));
            }
        }
    }
}
