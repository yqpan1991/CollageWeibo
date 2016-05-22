package apollo.edus.collageweibo.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.utils.ToastUtil;


/**
 * Created by wenmingvs on 16/5/2.
 */
public class PostActivity extends Activity {

    private Context mContext;
    private ImageView composeIdea;
    private ImageView composePhoto;
    private ImageView composeHeadlines;
    private ImageView composeLbs;
    private ImageView composeReview;
    private ImageView composeMore;
    private ImageView composeClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postfragment_layout);
        mContext = this;
        composeIdea = (ImageView) findViewById(R.id.compose_idea);
        composePhoto = (ImageView) findViewById(R.id.compose_photo);
        composeHeadlines = (ImageView) findViewById(R.id.compose_headlines);
        composeLbs = (ImageView) findViewById(R.id.compose_lbs);
        composeReview = (ImageView) findViewById(R.id.compose_review);
        composeMore = (ImageView) findViewById(R.id.compose_more);
        composeClose = (ImageView) findViewById(R.id.compose_close);

        composeIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EsUserManager.getInstance().hasLogIn()){
                    showLoginTipDialog();
                    return;
                }
                startActivity(new Intent(getApplicationContext(), IdeaActivity.class));
                finish();
            }
        });
        composePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EsUserManager.getInstance().hasLogIn()){
                    showLoginTipDialog();
                    return;
                }
                Intent intent = new Intent(mContext, IdeaActivity.class);
                intent.putExtra("startAlumbAcitivity", true);
                startActivity(intent);
                finish();
            }
        });
        composeHeadlines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发中...");
            }
        });
        composeLbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发中...");
            }
        });
        composeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发中...");
            }
        });
        composeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发中...");
            }
        });
        composeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void showLoginTipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("此功能需要登录,是否去登录?")
                .setCancelable(true)
                .setIcon(R.drawable.logo)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("取消", null);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
