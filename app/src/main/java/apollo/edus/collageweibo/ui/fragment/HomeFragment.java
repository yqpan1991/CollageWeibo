package apollo.edus.collageweibo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.ui.adapter.WeiboAdapter;
import apollo.edus.collageweibo.ui.widget.WeiboItemSapce;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.RecyclerViewUtils;

/**
 * Created by panyongqiang on 16/5/20.
 */
public class HomeFragment extends EsBaseFragment {
    private TextView mTvUserName;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    private WeiboAdapter mAdapter;
    private List<WeiboResult.WeiboInfo> mDatas;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;

    //首页需要支持的功能
    //1. 未登录状态,也可以看到附近的人的微博,下拉刷新,加载更多
    //2. 登录的状态下,也可以看到相关的人的微博,下拉刷新,加载更多
    //3. 需要不断的获取地理位置,将地理位置缓存,如果是已经登录的状态,上报给后台,未登录时,不上报
    //4. 微博详情,在登录的状态下,可以点赞,评论和转发,未登录的状态下,不能点赞,评论和转发(可以显示,然后点击是,提示用户即可)
    //5. 用户登录状态发生更改时,清空内存缓存的数据,然后清除网络请求,并且重新加载数据




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mainactivity_layout, container ,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvUserName = (TextView) view.findViewById(R.id.tv_username);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.weiboRecyclerView);
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mDatas = new ArrayList<>();
        mAdapter = new WeiboAdapter(mDatas, getActivity());
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        mRecyclerView.addItemDecoration(new WeiboItemSapce((int) getContext().getResources().getDimension(R.dimen.home_weiboitem_space)));
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadFromTop();
            }
        });
    }

    private void reloadFromTop() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //检测用户的登录和退出
        //对于用户登录和退出,都需要清除数据,重新获取数据

        //对于有地理位置的更新,如果是登录的用户,那么需要不断的通知服务端数据的更新
        //对于未登录的用户,不需要上报

    }

    public void scrollToTop(boolean refreshData){

    }
    
}
