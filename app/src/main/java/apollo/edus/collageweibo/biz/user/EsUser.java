package apollo.edus.collageweibo.biz.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Panda on 2016/5/21.
 */
public class EsUser {

    @SerializedName("userName")
    private String userName;
    @SerializedName("pwd")
    private String password;
    @SerializedName("userId")
    private String userId;



    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }


}
