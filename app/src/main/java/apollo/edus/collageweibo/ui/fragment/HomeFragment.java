package apollo.edus.collageweibo.ui.fragment;

import android.content.Intent;
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
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.ui.activity.LoginActivity;
import apollo.edus.collageweibo.ui.adapter.WeiboAdapter;
import apollo.edus.collageweibo.ui.mvp.impl.HomeFragmentPresentImpl;
import apollo.edus.collageweibo.ui.mvp.present.HomeFragmentPresent;
import apollo.edus.collageweibo.ui.mvp.view.HomeFragmentView;
import apollo.edus.collageweibo.ui.widget.WeiboItemSapce;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.RecyclerViewUtils;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.weight.LoadingFooter;

/**
 * Created by panyongqiang on 16/5/20.
 */
public class HomeFragment extends EsBaseFragment implements HomeFragmentView{
    private TextView mTvUserName;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    private WeiboAdapter mAdapter;
    private List<WeiboResult.WeiboInfo> mDatas;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private HomeFragmentPresent mPresent;

    //首页需要支持的功能
    //1. 未登录状态,也可以看到附近的人的微博,下拉刷新,加载更多,没有数据，点击屏幕重试
    //2. 登录的状态下,也可以看到相关的人的微博,下拉刷新,加载更多，没有数据，点击屏幕重试
    //3. 需要不断的获取地理位置,将地理位置缓存,如果是已经登录的状态,上报给后台,未登录时,不上报
    //4. 微博详情,在登录的状态下,可以点赞,评论和转发,未登录的状态下,不能点赞,评论和转发(可以显示,然后点击是,提示用户即可)
    //5. 用户登录状态发生更改时,清空内存缓存的数据,然后清除网络请求,并且重新加载数据


    //显示加载更多showLoadingMoreIcon
    //隐藏加载更多hideLoadingMoreIcon
    //提示没有更多数据 showEndFooterView
    //显示网络错误 showErrorFooterView
    //隐藏footerView，hideFooterView


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mainfragment_layout, container ,false);
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
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, mDatas.size(), LoadingFooter.State.Normal, null);
    }

    public EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mDatas != null && mDatas.size() > 0) {
                showLoadFooterView();
                mPresent.requestMoreData();
            }
        }
    };

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresent.pullToRefreshData();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresent = new HomeFragmentPresentImpl(this, getActivity());
        mPresent.start();
        //检测用户的登录和退出
        //对于用户登录和退出,都需要清除数据,重新获取数据
        //cancel request
        //clear memory cache data
        //show loading from top ui

        //对于有地理位置的更新,如果是登录的用户,那么需要不断的通知服务端数据的更新
        //对于未登录的用户,不需要上报

    }

    public void scrollToTop(boolean refreshData){
        mPresent.pullToRefreshData();
    }

    @Override
    public void updateListView(List<WeiboResult.WeiboInfo> statuselist) {
        mAdapter.setData(statuselist);
    }

    @Override
    public void showLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFooterView() {
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
    }

    @Override
    public void hideFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void showEndFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.TheEnd);
    }

    @Override
    public void showErrorFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.NetWorkError);
    }

    @Override
    public void showUserInfo(EsUserProfile userProfile) {
        if(userProfile == null){
            mTvUserName.setText("点击登录");
            mTvUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
        }else{
            mTvUserName.setText(userProfile.getDisplayName());
        }
    }

/*
    @Override
    public void showRecyclerView() {
        if (mSwipeRefreshLayout.getVisibility() != View.VISIBLE) {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideRecyclerView() {
        if (mSwipeRefreshLayout.getVisibility() != View.GONE) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyBackground() {
        if (mEmptyLayout.getVisibility() != View.VISIBLE) {
            mEmptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyBackground() {
        if (mEmptyLayout.getVisibility() != View.GONE) {
            mEmptyLayout.setVisibility(View.GONE);
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresent.stop();
    }
}
