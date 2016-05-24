package apollo.edus.collageweibo.biz.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import apollo.edus.collageweibo.biz.user.EsUserProfile;

/**
 * Created by panyongqiang on 16/5/24.
 */
public class UserResult {
    @SerializedName("hasNextPage")
    private boolean hasNextPage;

    @SerializedName("list")
    private List<EsUserProfile> list;

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean hasNextPage(){
        //TODO ------- just for test
        return false;
    }

    public List<EsUserProfile> getList() {
        return list;
    }

    public void setList(List<EsUserProfile> list) {
        this.list = list;
    }
}
