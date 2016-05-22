package apollo.edus.collageweibo.biz.net.request;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by Panda on 2016/5/21.
 */
public class CustomJSONObjectRequest extends JsonObjectRequest {


    public CustomJSONObjectRequest(int method, String url, String requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, new JsonReponseListenerWrapper(listener, errorListener), errorListener);
    }

    public CustomJSONObjectRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this(0, url, (String)null, listener, errorListener);
    }

    public CustomJSONObjectRequest(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this(method, url, (String)null, listener, errorListener);
    }

    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this(method, url, jsonRequest == null?null:jsonRequest.toString(), listener, errorListener);
    }

    public CustomJSONObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this(jsonRequest == null?0:1, url, jsonRequest, listener, errorListener);
    }
}
