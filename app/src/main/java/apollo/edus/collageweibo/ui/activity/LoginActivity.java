package apollo.edus.collageweibo.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.global.EsGlobal;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.biz.user.EsUser;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.utils.ToastUtil;


/**
 * Created by wenmingvs on 16/5/2.
 */
public class LoginActivity extends Activity {
    private final String TAG = this.getClass().getSimpleName();

    private boolean isInited = false;

    private EditText mEtUserName;
    private EditText mEtPassowrd;

    private ProgressDialog mProgressDialog;
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        if(EsUserManager.getInstance().hasLogIn()){
            finish();
            return;
        }
        isInited= true;
        initView();
        initData();
    }

    private void initView() {
        Button mBtLogin= (Button) findViewById(R.id.btn_login);
        mEtUserName = (EditText) findViewById(R.id.username);
        mEtPassowrd = (EditText) findViewById(R.id.password);
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        if(isLogin){
            Toast.makeText(EsGlobal.getGlobalContext(), "正在登录中,请稍后...", Toast.LENGTH_SHORT).show();
            return;
        }
        String userName = mEtUserName.getText().toString();
        String password = mEtPassowrd.getText().toString();
        if(TextUtils.isEmpty(userName) ||  TextUtils.isEmpty(password)){
            Toast.makeText(EsGlobal.getGlobalContext(), "数据不完整,请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("登录中");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        isLogin = true;
        EsApiHelper.userLogin(userName, password, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e(TAG,"SUC:"+s);
                handleUserLoginSuc(s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,"error:"+volleyError.toString());
            }
        });
    }

    private void handleUserLoginSuc(String s) {
        Toast.makeText(EsGlobal.getGlobalContext(), "result:"+s, Toast.LENGTH_SHORT).show();
        //save info
        //parse json, save to UserProfile and EsUser
        Gson gson = new Gson();
        EsUser esUser = gson.fromJson(s, EsUser.class);
        EsUserManager userManager = EsUserManager.getInstance();
        userManager.setUserInfo(esUser);
        userManager.saveUserInfoToNative();
        userManager.setHasLogin(true);
        userManager.setUserProfile(new Gson().fromJson(s, EsUserProfile.class));
        Log.e(TAG, new Gson().toJson(userManager.getUserProfile()));
        userManager.notifyUserLogin();
        isLogin  = false;
        mProgressDialog.dismiss();
        mProgressDialog = null;
        finish();
    }

    private void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
