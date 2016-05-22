package apollo.edus.collageweibo.biz.net.request;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import apollo.edus.collageweibo.biz.net.api.EsApiResultHelper;

/**
 * Created by Panda on 2016/5/21.
 */
public class JsonReponseListenerWrapper implements Response.Listener<JSONObject> {
    public Response.Listener<JSONObject> mSucListener;
    private Response.ErrorListener mErrorListener;

    public JsonReponseListenerWrapper(Response.Listener<JSONObject> sucListener, Response.ErrorListener errorListener) {
        mSucListener = sucListener;
        mErrorListener = errorListener;
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        if (EsApiResultHelper.isSuc(jsonObject)) {
            if (mSucListener != null) {
                mSucListener.onResponse(jsonObject);
            }
        } else {
            if (mErrorListener != null) {
                mErrorListener.onErrorResponse(new VolleyError(EsApiResultHelper.getErrorString(jsonObject)));
            }
        }
    }
}
