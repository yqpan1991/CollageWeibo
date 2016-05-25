package apollo.edus.collageweibo.ui.mvp.view;

import java.util.List;

import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.user.EsUserProfile;

/**
 * Created by wenmingvs on 16/5/16.
 */
public interface HomeFragmentView {
    public void updateListView(List<WeiboResult.WeiboInfo> statuselist);

    /**
     * 显示loading动画
     */
    public void showLoadingIcon();

    /**
     * 隐藏loadding动画
     */
    public void hideLoadingIcon();

    /**
     * 显示正在加载的FooterView
     */
    public void showLoadFooterView();

    /**
     * 隐藏FooterView
     */
    public void hideFooterView();

    /**
     * 显示FooterView，提示没有任何内容了
     */
    public void showEndFooterView();

    /**
     * 显示FooterView，提示没有网络
     */
    public void showErrorFooterView();

    public void showUserInfo(EsUserProfile profile);

/*    public void showRecyclerView();

    public void hideRecyclerView();

    public void showEmptyBackground();

    public void hideEmptyBackground();*/

}
