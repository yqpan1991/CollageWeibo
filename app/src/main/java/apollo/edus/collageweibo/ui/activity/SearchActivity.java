package apollo.edus.collageweibo.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.bean.UserResult;
import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.global.EsGlobal;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.biz.user.EsUser;
import apollo.edus.collageweibo.ui.adapter.LvFriendsAdapter;
import apollo.edus.collageweibo.ui.widget.EsSearchContactEditText;
import apollo.edus.collageweibo.ui.widget.XListView;
import apollo.edus.collageweibo.utils.ListViewUtil;
import apollo.edus.collageweibo.utils.NetworkUtils;
import apollo.edus.collageweibo.utils.ToastUtil;

/**
 * Created by panyongqiang on 16/5/24.
 */
public class SearchActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();

    private ProgressDialog progressDialog;
    private TextView tvEmpty;
    private View rlLoading;
    private XListView xListView;
    private View vUserInfo;

    private final int STATUS_INIT = 0;
    private final int STATUS_LOADING = 1;
    private final int STATUS_SHOW_RESULT = 2;
    private final int STATUS_ERROR = 3;
    private int curStatus = 0;

    private int pageNum = -1;

    private LvFriendsAdapter adapter;

    private EsSearchContactEditText editText;
    private TextView tvMyInfo;
    private View headView;
    private InputMethodManager inputMethodManager;
    private String lastSearchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_contact);
        initView();
        initData();

        //search --> result
        //click show user detail
        //detail show relationShip
        //this is OK
        //界面上不提供操作，只有点击进入详情后，才能添加删除关注的操作
    }

    private void initView() {
        headView = findViewById(R.id.search_contact_head);
        tvMyInfo = (TextView) findViewById(R.id.tv_my_zapya_info);
        editText = (EsSearchContactEditText) findViewById(R.id.edit_note);
        editText.setSearchContactListener(searchContactListener);
        vUserInfo = findViewById(R.id.fl_userlist);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        rlLoading = findViewById(R.id.rl_loading);
        xListView = (XListView) findViewById(R.id.abslistid);
        ListViewUtil.removeOverscrollEffect(xListView, true);
        xListView.setEmptyView(tvEmpty);
        xListView.setPullRefreshEnable(false);
        xListView.setPullLoadEnable(false);
        xListView.setXListViewListener(xListViewListener);
        xListView.setOnScrollListener(onScrollListener);
        xListView.setOnItemClickListener(onItemClickListener);
    }

    private void initData() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        adapter = new LvFriendsAdapter(this);
        xListView.setAdapter(adapter);
    }

    private EsSearchContactEditText.EsSearchContactListener searchContactListener = new EsSearchContactEditText.EsSearchContactListener() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void onCancelClicked() {
            finish();
        }

        @Override
        public void onSearchClicked() {
            searchContact();
        }

        @Override
        public void onClearClicked() {
            hideInputMethod();
        }

    };

    private void resetSearchInfo() {
        lastSearchName = null;
        pageNum = 0;
    }

    /**
     * 查找contact
     */
    private void searchContact() {
        // 判断是否相同
        final String name = editText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            resetSearchInfo();
            adapter.setData(null);
            ToastUtil.showShort(EsGlobal.getGlobalContext(), "请输入用户名");
            return;
        }

        // 如果上次搜索和本次搜索名字没有变化（针对搜索用户名，那么不再重新加载）
        if ((curStatus == STATUS_LOADING || curStatus == STATUS_SHOW_RESULT)
                && (name.equals(lastSearchName))) {
            return;
        }
        resetSearchInfo();
        hideInputMethod();
        lastSearchName = name;
        setStatus(STATUS_LOADING, null, true);
        loadData(lastSearchName, pageNum);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            if (position < xListView.getHeaderViewsCount()
                    || (position >= xListView.getHeaderViewsCount()
                    + adapter.getCount())) {
                return;
            }
            int adapterPosition = position - xListView.getHeaderViewsCount();
/*            UserInfo item = adapter.getItem(adapterPosition);
            showUserProfileActivity(item.id);*/
        }

    };

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub

        }
    };

    private XListView.IXListViewListener xListViewListener = new XListView.IXListViewListener() {

        @Override
        public void onRefresh() {
            // based on our logic , this never gonna happen
        }

        @Override
        public void onLoadMore() {
            // Toast.makeText(getApplicationContext(), "load more",
            // Toast.LENGTH_SHORT).show();
            pageNum++;
            loadData(lastSearchName, pageNum);
        }
    };

    private void loadData(final String name, final int pageNum) {
        if (name == null) {
            return;
        }
        EsApiHelper.searchUser(name, pageNum, new Response.Listener<String>() {

            @Override
            public void onResponse(String string) {
                Gson gson = new Gson();
                UserResult userResult = gson.fromJson(string, UserResult.class);
                showData(userResult, pageNum);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        });
    }


    protected void handleError(VolleyError error) {
        String errorTip = null;
        if (error.networkResponse == null) {
            boolean internetConnected = NetworkUtils
                    .isNetworkAvailable(this);
            if (internetConnected) {
                errorTip = "查询失败，请稍后重试";
            } else {
                errorTip = "查询失败，请联网后重试";
            }
        } else {
            errorTip = "查询失败，请稍后重试";
        }
        adapter.setData(null);
        setStatus(STATUS_ERROR, errorTip, false);
    }


    private void setStatus(int status, String errMsg, boolean hasMore) {
        curStatus = status;
        xListView.stopLoadMore();
        if (status == STATUS_INIT) {
            xListView.setPullLoadEnable(false);
            vUserInfo.setVisibility(View.GONE);
        } else if (status == STATUS_LOADING) {
            xListView.setPullLoadEnable(false);
            vUserInfo.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            rlLoading.setVisibility(View.VISIBLE);
            xListView.setVisibility(View.GONE);
        } else if (status == STATUS_SHOW_RESULT) {
            vUserInfo.setVisibility(View.VISIBLE);
            tvEmpty.setText("没有相关的搜索结果");
            tvEmpty.setVisibility(View.GONE);
            rlLoading.setVisibility(View.GONE);
            xListView.setVisibility(View.VISIBLE);
            xListView.setPullLoadEnable(hasMore);
        } else if (status == STATUS_ERROR) {// error
            xListView.setPullLoadEnable(false);
            vUserInfo.setVisibility(View.VISIBLE);
            tvEmpty.setText(errMsg);
            tvEmpty.setVisibility(View.VISIBLE);
            rlLoading.setVisibility(View.GONE);
            xListView.setVisibility(View.VISIBLE);
        }
    }


    protected void showData(UserResult searchUserInfo, int curPageNum) {
        setStatus(STATUS_SHOW_RESULT, null, searchUserInfo.hasNextPage());
        if (lastSearchName != null) {
            if (curPageNum == 0) {
                adapter.setData(searchUserInfo.getList());
            } else {
                adapter.addData(searchUserInfo.getList());
            }
        }
    }

    public void hideInputMethod() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
