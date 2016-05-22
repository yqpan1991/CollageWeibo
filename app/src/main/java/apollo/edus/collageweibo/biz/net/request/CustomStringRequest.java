package apollo.edus.collageweibo.biz.net.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Panda on 2016/3/6.
 */
public class CustomStringRequest extends StringRequest {

    public CustomStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, new ResponseListenerWrapper(listener, errorListener), errorListener);
    }

    public CustomStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, new ResponseListenerWrapper(listener, errorListener), errorListener);
    }


}
