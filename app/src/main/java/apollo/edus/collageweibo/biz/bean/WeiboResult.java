package apollo.edus.collageweibo.biz.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import apollo.edus.collageweibo.biz.user.EsUserProfile;

/**
 * Created by panyongqiang on 16/5/23.
 */
public class WeiboResult {

    @SerializedName("hasNextPage")
    private boolean hasNextPage;

    @SerializedName("list")
    private List<WeiboInfo> list;

    public boolean getHasNextPage() {
        return hasNextPage;
    }

    public  boolean hasNextPage(){
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<WeiboInfo> getList() {
        return list;
    }

    public void setList(List<WeiboInfo> list) {
        this.list = list;
    }

    public static class WeiboInfo implements Serializable{
        @SerializedName("weibo_id")
        private String weiboId;
        @SerializedName("userid")
        private String userid;
        @SerializedName("create_time")
        private long createTime;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("is_forwarding")
        private boolean isForwarding;
        @SerializedName("content")
        private String content;
        @SerializedName("mfay")
        private int mfay;
        @SerializedName("mreply")
        private int mreply;
        @SerializedName("mcopy")
        private int mcopy;
//        @SerializedName("orgin_message")
        private WeiboInfo orginMessage;


        @SerializedName("message_file")
        private List<?> message_file;

        @SerializedName("comments")
        private List<CommentInfo> commentInfoList;

        @SerializedName("userinfo")
        private EsUserProfile userInfo;

        public String getWeiboId() {
            return weiboId;
        }

        public void setWeiboId(String weiboId) {
            this.weiboId = weiboId;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public boolean getForwarding() {
            return isForwarding;
        }

        public void setForwarding(boolean forwarding) {
            this.isForwarding = forwarding;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getMfay() {
            return mfay;
        }

        public void setMfay(int mfay) {
            this.mfay = mfay;
        }

        public int getMreply() {
            return mreply;
        }

        public void setMreply(int mreply) {
            this.mreply = mreply;
        }

        public int getMcopy() {
            return mcopy;
        }

        public void setMcopy(int mcopy) {
            this.mcopy = mcopy;
        }

        public WeiboInfo getOrginMessage() {
            return orginMessage;
        }

        public void setOrginMessage(WeiboInfo orgin_message) {
            this.orginMessage = orgin_message;
        }

        public List<?> getMessage_file() {
            return message_file;
        }

        public void setMessage_file(List<?> message_file) {
            this.message_file = message_file;
        }

        public List<CommentInfo> getCommentInfoList() {
            return commentInfoList;
        }

        public void setCommentInfoList(List<CommentInfo> commentInfoList) {
            this.commentInfoList = commentInfoList;
        }

        public EsUserProfile getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(EsUserProfile userInfo) {
            this.userInfo = userInfo;
        }

        @Override
        public int hashCode() {
            return weiboId.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof WeiboInfo){
                WeiboInfo compare = (WeiboInfo) o;
                return compare.weiboId.equals(weiboId);
            }
            return false;
        }



        public static class CommentInfo implements Serializable{
            @SerializedName("userId")
            private String userId;
            @SerializedName("nickName")
            private String nickName;
            @SerializedName("content")
            private String content;
            @SerializedName("createTime")
            private long createTime;
        }
    }
}
