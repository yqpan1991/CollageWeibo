package apollo.edus.collageweibo.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.global.EsGlobal;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.biz.user.EsUser;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Panda on 2016/5/22.
 */
public class RegisterActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.username)
    EditText mEtUsername;
    @BindView(R.id.password)
    EditText mEtPassword;
    @BindView(R.id.et_nickname)
    EditText mEtNickname;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        ButterKnife.bind(this);
        initData();
    }


    private void initData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        String userName = mEtUsername.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        String nickName = mEtNickname.getText().toString().trim();
        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nickName)){
            Toast.makeText(EsGlobal.getGlobalContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("登录中");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        EsApiHelper.registerUser(userName, password, nickName, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                handleRegisterSucceed(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
                Toast.makeText(EsGlobal.getGlobalContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG,"error:"+volleyError.toString());
            }
        });


    }

    private void handleRegisterSucceed(String s) {
        //save profile
        //save userinfo
        //dismiss dialog
        //notify login succeed
        Gson gson = new Gson();
        EsUser esUser = gson.fromJson(s, EsUser.class);
        EsUserManager userManager = EsUserManager.getInstance();
        userManager.setUserInfo(esUser);
        userManager.saveUserInfoToNative();
        userManager.setHasLogin(true);

        userManager.notifyUserLogin();

        mProgressDialog.dismiss();
        mProgressDialog = null;
        finish();

    }
}
