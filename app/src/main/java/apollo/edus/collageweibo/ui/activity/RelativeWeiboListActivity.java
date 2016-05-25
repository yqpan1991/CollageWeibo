package apollo.edus.collageweibo.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.ui.adapter.WeiboAdapter;
import apollo.edus.collageweibo.ui.mvp.impl.MyWeiBoActivityPresentImp;
import apollo.edus.collageweibo.ui.mvp.impl.RelativeWeiboListActivityPresentImp;
import apollo.edus.collageweibo.ui.mvp.present.MyWeiBoActivityPresent;
import apollo.edus.collageweibo.ui.mvp.present.RelativeWeiboListActivityPresent;
import apollo.edus.collageweibo.ui.mvp.view.MyWeiBoActivityView;
import apollo.edus.collageweibo.ui.mvp.view.RelativeWeiboListActivityView;
import apollo.edus.collageweibo.ui.widget.WeiboItemSapce;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import apollo.edus.collageweibo.ui.widget.endlessrecyclerview.weight.LoadingFooter;

/**
 * Created by panyongqiang on 16/5/23.
 */
public class RelativeWeiboListActivity extends Activity implements RelativeWeiboListActivityView {
    public static final String EXTRA_TYPE = "extra_type";

    public WeiboAdapter mAdapter;
    private List<WeiboResult.WeiboInfo> mDatas = new ArrayList<>();
    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public boolean mRefrshAllData;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private RelativeWeiboListActivityPresent mPresent;
    private TextView mTvTitle;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_weibo_layout);
        mContext = this;
        mType = getIntent().getIntExtra(EXTRA_TYPE,0);
        mPresent = new RelativeWeiboListActivityPresentImp(this, mType);
        initRefreshLayout();
        initRecyclerView();
        initTitle();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mPresent.pullToRefreshData(EsUserManager.getInstance().getUserInfo().getUserId(), mContext);
            }
        });
    }

    private void initTitle() {
        mTvTitle = (TextView) findViewById(R.id.foldername);
        String content = "全部微博";
        switch (mType){
            case 1:
                content = "我点评的";
                break;
            case 2:
                content = "我赞的";
                break;
            case 3:
                content = "@我的";
                break;
        }
        mTvTitle.setText(content);
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
                mPresent.pullToRefreshData(EsUserManager.getInstance().getUserInfo().getUserId(), mContext);
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
        mRecyclerView.addItemDecoration(new WeiboItemSapce((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_space)));
        RecyclerViewStateUtils.setFooterViewState(this, mRecyclerView, mDatas.size(), LoadingFooter.State.Normal, null);
    }

    public EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mDatas != null && mDatas.size() > 0) {
                showLoadFooterView();
                mPresent.requestMoreData(EsUserManager.getInstance().getUserInfo().getUserId(), mContext);
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
        RecyclerViewStateUtils.setFooterViewState(this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
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

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

}
