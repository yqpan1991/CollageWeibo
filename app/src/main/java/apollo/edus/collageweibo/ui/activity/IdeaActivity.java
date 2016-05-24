package apollo.edus.collageweibo.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.bean.AlbumFolderInfo;
import apollo.edus.collageweibo.biz.bean.ImageInfo;
import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.ui.imglist.ImgListAdapter;
import apollo.edus.collageweibo.utils.FillContent;
import apollo.edus.collageweibo.utils.ToastUtil;

/**
 * Created by wenmingvs on 16/5/2.
 */
public class IdeaActivity extends Activity implements ImgListAdapter.OnFooterViewClickListener {

    private final String TAG = this.getClass().getSimpleName();

//    private StatusesAPI mStatusesAPI;
//    private Oauth2AccessToken mAccessToken;
//    private UsersAPI mUsersAPI;

    private Context mContext;
    private TextView mCancal;
    private TextView mUserName;
    private TextView mSendButton;
    private TextView publicbutton;
    private ImageView picture;
    private ImageView mentionbutton;
    private ImageView trendbutton;
    private ImageView emoticonbutton;
    private ImageView more_button;
    private EditText mEditText;
    private TextView mLimitTextView;

    private LinearLayout mRepostlayout;
    private ImageView repostImg;
    private TextView repostName;
    private TextView repostContent;
    private RecyclerView mRecyclerView;
    private ScrollView mScrollView;

    private ArrayList<AlbumFolderInfo> mFolderList = new ArrayList<AlbumFolderInfo>();
    private ArrayList<ImageInfo> mSelectImgList = new ArrayList<ImageInfo>();
    private WeiboResult.WeiboInfo mStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_idea_layout);
        mContext = this;


        mCancal = (TextView) findViewById(R.id.idea_cancal);
        mUserName = (TextView) findViewById(R.id.idea_username);
        mSendButton = (TextView) findViewById(R.id.idea_send);
        publicbutton = (TextView) findViewById(R.id.publicbutton);
        picture = (ImageView) findViewById(R.id.picture);
        mentionbutton = (ImageView) findViewById(R.id.mentionbutton);
        trendbutton = (ImageView) findViewById(R.id.trendbutton);
        emoticonbutton = (ImageView) findViewById(R.id.emoticonbutton);
        more_button = (ImageView) findViewById(R.id.more_button);
        mEditText = (EditText) findViewById(R.id.idea_content);
        mLimitTextView = (TextView) findViewById(R.id.limitTextView);

        mRepostlayout = (LinearLayout) findViewById(R.id.repost_layout);
        repostImg = (ImageView) findViewById(R.id.repost_img);
        repostName = (TextView) findViewById(R.id.repost_name);
        repostContent = (TextView) findViewById(R.id.repost_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.ImgList);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);

//        initAccessToken();
        initContent();
        setUpListener();

        if (getIntent().getBooleanExtra("startAlumbAcitivity", false) == true) {
            Intent intent = new Intent(IdeaActivity.this, AlbumActivity.class);
            intent.putParcelableArrayListExtra("selectedImglist", mSelectImgList);
            startActivityForResult(intent, 0);
        }
    }

/*    private void initAccessToken() {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        mUsersAPI = new UsersAPI(mContext, Constants.APP_KEY, mAccessToken);
    }*/


    /**
     * 填充内容，
     * 1. 转发的内容是转发微博，
     * 2. 转发的内容是原创微博，
     */
    private void initContent() {
        refreshUserName();
        mStatus = (WeiboResult.WeiboInfo) getIntent().getSerializableExtra("status");

        if (mStatus == null) {
            return;
        }

        mRepostlayout.setVisibility(View.VISIBLE);
        mEditText.setHint("说说分享的心得");

        if(mStatus.getForwarding() && mStatus.getOrginMessage() != null){//转发的内容是转发微博
            Toast.makeText(this, "未处理转发的微博", Toast.LENGTH_SHORT).show();
        }else{
            //转发的内容是原创微博
            FillContent.FillCenterContent(mStatus, repostImg, repostName, repostContent);
            String content = mEditText.getText().toString();
            if (content.trim().isEmpty()) {
                mEditText.getText().append("转发微博");
            }
        }
        changeSendButtonBg(mEditText.getText().toString().length());

     /*   //1. 转发的内容是转发微博
        if (mStatus.retweeted_status != null) {
            mEditText.setText(WeiBoContentTextUtil.getWeiBoContent("//@" + mStatus.user.name + ":" + mStatus.text, mContext, mEditText));
            FillContent.FillCenterContent(mStatus.retweeted_status, repostImg, repostName, repostContent);
            mEditText.setSelection(0);

        }
        //2. 转发的内容是原创微博
        else if (mStatus.retweeted_status == null) {
            FillContent.FillCenterContent(mStatus, repostImg, repostName, repostContent);
            String content = mEditText.getText().toString();
            if (content.trim().isEmpty()) {
                mEditText.getText().append("转发微博");
            }

        }
        changeSendButtonBg(mEditText.getText().toString().length());*/
    }

    private void refreshUserName() {
        mUserName.setText(EsUserManager.getInstance().getUserInfo().getUserName());
    }


    /**
     * 设置监听事件
     */
    private void setUpListener() {
        mCancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdeaActivity.this, AlbumActivity.class);
                intent.putParcelableArrayListExtra("selectedImglist", mSelectImgList);
                startActivityForResult(intent, 0);
            }
        });
        mentionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.getText().insert(mEditText.getSelectionStart(), "@");
            }
        });
        trendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.getText().insert(mEditText.getSelectionStart(), "##");
                mEditText.setSelection(mEditText.getSelectionStart() - 1);
            }
        });
        emoticonbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发此功能...");
            }
        });
        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发此功能...");
            }
        });

        mEditText.addTextChangedListener(watcher);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //没有图片，且也没有文本内容，识别为空
                if (mSelectImgList.size() == 0 && (mEditText.getText().toString().isEmpty() || mEditText.getText().toString().length() == 0)) {
                    ToastUtil.showShort(mContext, "发送的内容不能为空");
                    return;
                }

                if (mEditText.getText().toString().length() > 140) {
                    ToastUtil.showShort(mContext, "文本超出限制140个字！请做调整");
                    return;
                }

                if (mSelectImgList.size() > 1) {
                    ToastUtil.showShort(mContext, "目前仅支持一张图片上传");
                    return;
                }

                if(mSelectImgList.isEmpty()){
                    EsApiHelper.shareContentWeibo(mEditText.getText().toString(), new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {
                            Log.e(TAG,"SUC:"+s);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e(TAG,"fail:"+volleyError.toString());
                        }
                    });
                }else{
                    //send
                    EsApiHelper.shareImageWeibo(mEditText.getText().toString(), mSelectImgList, new Response.Listener<String>(){

                        @Override
                        public void onResponse(String s) {
                            Toast.makeText(getApplicationContext(), "suc:"+s, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), "error:"+volleyError, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                finish();
            }
        });

    }


    private TextWatcher watcher = new TextWatcher() {
        private CharSequence inputString;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            inputString = s;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            changeSendButtonBg(inputString.toString().length());
            if (inputString.length() > 140) {
                int outofnum = inputString.length() - 140;
                mLimitTextView.setText("-" + outofnum + "");
            } else {
                mLimitTextView.setText("");
            }
        }
    };

    /**
     * 根据输入的文本数量，决定发送按钮的背景
     *
     * @param length
     */
    private void changeSendButtonBg(int length) {

        if (length > 0) {
            highlightSendButton();
        } else {
            sendNormal();
        }
    }

    private void highlightSendButton() {
        mSendButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.compose_send_corners_highlight_bg));
        mSendButton.setTextColor(Color.parseColor("#fbffff"));
        mSendButton.setEnabled(true);
    }

    private void sendNormal() {
        mSendButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.compose_send_corners_bg));
        mSendButton.setTextColor(Color.parseColor("#b3b3b3"));
        mSendButton.setEnabled(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data != null) {
                    mSelectImgList = data.getParcelableArrayListExtra("selectImgList");
                    initImgList();
                    changeSendButtonBg(mSelectImgList.size());
                }
                break;
        }
    }


    public void initImgList() {
        mRecyclerView.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        ImgListAdapter imgListAdapter = new ImgListAdapter(mSelectImgList, mContext);
        imgListAdapter.setOnFooterViewClickListener(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(imgListAdapter);
    }

    @Override
    public void OnFooterViewClick() {
        Intent intent = new Intent(IdeaActivity.this, AlbumActivity.class);
        intent.putParcelableArrayListExtra("selectedImglist", mSelectImgList);
        startActivityForResult(intent, 0);
    }


}
