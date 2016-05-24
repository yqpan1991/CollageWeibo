package apollo.edus.collageweibo.biz.net.api;

/**
 * Created by Panda on 2016/5/21.
 */
public class EsApi {

    private static final String HOST;

    static{
        HOST = "http://123.57.9.103:83/app/";
    }

    public static String getHost(){
        return HOST;
    }

    public final static String RELATIVE_URL = "?por=%s&userid=%s&type=%s&pageSize=%s";

    public static String getFullUrl(String url){
        return HOST + url;
    }

    public static String getFullUrl(String url,String... params){
        return String.format(HOST+url,params);
    }
}
