package apollo.edus.collageweibo.biz.net.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Panda on 2016/5/21.
 */
public class EsApiResultHelper {
    public static boolean isSuc(JSONObject jsonObject) {
        return jsonObject != null && jsonObject.optInt(EsApiKeys.KEY_CODE, -1) == 0;
    }

    public static String getErrorString(JSONObject jsonObject) {
        return jsonObject.optString(EsApiKeys.KEY_MESSAGE, "");
    }
}
