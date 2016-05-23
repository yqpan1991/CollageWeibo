package apollo.edus.collageweibo.biz.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by panyongqiang on 16/5/23.
 */
public class WeiboResult {

    @SerializedName("hasNextPage")
    private int hasNextPage;

    @SerializedName("list")
    private List<WeiboInfo> list;

    public int getHasNextPage() {
        return hasNextPage;
    }

    public  boolean hasNextPage(){
        return hasNextPage == 1;
    }

    public void setHasNextPage(int hasNextPage) {
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
        private String weibo_id;
        @SerializedName("userid")
        private String userid;
//        @SerializedName("create_time")
//        private long create_time;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("is_forwarding")
        private String is_forwarding;
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

        public String getWeibo_id() {
            return weibo_id;
        }

        public void setWeibo_id(String weibo_id) {
            this.weibo_id = weibo_id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

/*        public long getCreate_time() {
            return create_time;
        }*/

/*        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }*/

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getIs_forwarding() {
            return is_forwarding;
        }

        public void setIs_forwarding(String is_forwarding) {
            this.is_forwarding = is_forwarding;
        }

        public boolean isForwarding(){
            return TextUtils.isEmpty(is_forwarding) && "2".equals(is_forwarding);
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

        @Override
        public int hashCode() {
            return weibo_id.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof WeiboInfo){
                WeiboInfo compare = (WeiboInfo) o;
                return compare.weibo_id.equals(weibo_id);
            }
            return false;
        }

        public static class CommentInfo{
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
