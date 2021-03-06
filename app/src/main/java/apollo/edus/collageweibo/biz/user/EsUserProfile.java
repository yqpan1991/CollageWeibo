package apollo.edus.collageweibo.biz.user;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Panda on 2016/5/21.
 */
public class EsUserProfile implements Serializable{

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
    private String admissionTime;
    @SerializedName("birth")
    private String birth;
    @SerializedName("sex")
    private int sex;

    @SerializedName("weibo")
    private long weibo;
    @SerializedName("fans")
    private long fans;
    @SerializedName("attention")
    private long attention;

    @SerializedName("distance")
    private String distance;


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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public Date getAdmissionTime() {
        if(TextUtils.isEmpty(admissionTime)){
            return null;
        }else{
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(admissionTime);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void setAdmissionTime(Date admissionTime) {
        if(admissionTime != null){
            this.admissionTime = new SimpleDateFormat("yyyy-MM-dd").format(admissionTime);
        }else{
            this.admissionTime = null;
        }
    }

    public Date getBirth() {
        if(TextUtils.isEmpty(admissionTime)){
            return null;
        }else{
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(admissionTime);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void setBirth(Date birth) {
        if(admissionTime != null){
            this.birth = new SimpleDateFormat("yyyy-MM-dd").format(admissionTime);
        }else{
            this.birth = null;
        }
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getWeibo() {
        return weibo;
    }

    public long getFans() {
        return fans;
    }

    public long getAttention() {
        return attention;
    }

    public void setWeibo(long weibo) {
        this.weibo = weibo;
    }

    public void setFans(long fans) {
        this.fans = fans;
    }

    public void setAttention(long attention) {
        this.attention = attention;
    }

    public String getDisplayName(){
        if(TextUtils.isEmpty(nickName)){
            return userName;
        }else{
            return nickName;
        }
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public long getValidDistance(){
        if(TextUtils.isEmpty(distance)){
            return -1;
        }
        return Long.valueOf(distance);
    }
}
