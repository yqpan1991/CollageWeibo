package apollo.edus.collageweibo.biz.user;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Panda on 2016/5/21.
 */
public class EsUserProfile {

    //parse server info
    @SerializedName("id")
    private String userId;
    @SerializedName("img")
    private String avatorUrl;
    @SerializedName("username")
    private String userName;
    @SerializedName("nickname")
    private String nickName;
    @SerializedName("user_sgin")
    private String userSgin;//签名
    @SerializedName("school")
    private String school;
    @SerializedName("admission_time")
    private Date admissionTime;
    @SerializedName("birth")
    private Date birth;
    @SerializedName("sex")
    private int sex;

    public String getAvatorUrl() {
        return avatorUrl;
    }

    public void setAvatorUrl(String avatorUrl) {
        this.avatorUrl = avatorUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserSgin() {
        return userSgin;
    }

    public void setUserSgin(String userSgin) {
        this.userSgin = userSgin;
    }


    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Date getAdmissionTime() {
        return admissionTime;
    }

    public void setAdmissionTime(Date admissionTime) {
        this.admissionTime = admissionTime;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
