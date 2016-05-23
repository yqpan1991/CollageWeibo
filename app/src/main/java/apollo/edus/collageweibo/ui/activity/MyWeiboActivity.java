package apollo.edus.collageweibo.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.ui.adapter.WeiboAdapter;
import apollo.edus.collageweibo.ui.mvp.impl.MyWeiBoActivityPresentImp;
import apollo.edus.collageweibo.ui.mvp.present.MyWeiBoActivityPresent;
import apollo.edus.collageweibo.ui.mvp.view.MyWeiBoActivityView;
import apollo.edus.collageweibo.ui.widget.SeachHeadView;
import apollo.edus.collageweibo.ui.widget.WeiboItemSapce;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.RecyclerViewUtils;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.weight.LoadingFooter;

/**
 * Created by panyongqiang on 16/5/23.
 */
public class MyWeiboActivity extends Activity implements MyWeiBoActivityView{

    public WeiboAdapter mAdapter;
    private List<WeiboResult.WeiboInfo> mDatas;
    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public boolean mRefrshAllData;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private MyWeiBoActivityPresent mMyWeiBoActivityPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilefragment_myweibo_layout);
        mContext = this;
        mMyWeiBoActivityPresent = new MyWeiBoActivityPresentImp(this);
        initRefreshLayout();
        initRecyclerView();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mMyWeiBoActivityPresent.pullToRefreshData(EsUserManager.getInstance().getUserInfo().getUserId(), mContext);
            }
        });
    }

    protected void initRefreshLayout() {
        mRefrshAllData = true;
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMyWeiBoActivityPresent.pullToRefreshData(EsUserManager.getInstance().getUserInfo().getUserId(), mContext);
            }
        });
    }

    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mAdapter = new WeiboAdapter(mDatas, mContext);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
//        RecyclerViewUtils.setHeaderView(mRecyclerView, new SeachHeadView(mContext));
        mRecyclerView.addItemDecoration(new WeiboItemSapce((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_space)));
    }

    public EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mDatas != null && mDatas.size() > 0) {
                showLoadFooterView();
                mMyWeiBoActivityPresent.requestMoreData(EsUserManager.getInstance().getUserInfo().getUserId(), mContext);
            }
        }
    };

    @Override
    public void updateListView(List<WeiboResult.WeiboInfo> statuselist) {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mDatas = statuselist;
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
        RecyclerViewStateUtils.setFooterViewState(MyWeiboActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
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
}
