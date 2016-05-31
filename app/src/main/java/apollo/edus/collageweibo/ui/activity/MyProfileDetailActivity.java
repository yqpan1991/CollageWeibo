package apollo.edus.collageweibo.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.global.EsGlobal;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.utils.NetworkUtils;
import apollo.edus.collageweibo.utils.ToastUtil;

/**
 * Created by Panda on 2016/5/22.
 */
public class MyProfileDetailActivity extends Activity {
    TextView tvSave;
    TextView tvNickname;
    EditText etNickname;
    TextView tvSignature;
    EditText etSignature;
    TextView tvGender;
    Spinner spGender;
    TextView tvBirth;
    TextView tvSchool;
    EditText etSchool;

    private EsUserProfile userProfile;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile_detail_layout);
        initView();
        initData();

    }

    private void initView() {
        tvSave = (TextView) findViewById(R.id.tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveData();
            }
        });
        etNickname = (EditText) findViewById(R.id.et_nickname);
        etSignature = (EditText) findViewById(R.id.et_signature);
        spGender = (Spinner) findViewById(R.id.sp_gender);
        etSchool = (EditText) findViewById(R.id.et_school);
    }

    private void handleSaveData() {
        if(!NetworkUtils.isNetworkAvailable(EsGlobal.getGlobalContext())){
            Toast.makeText(EsGlobal.getGlobalContext(), "网络未连接,请联网后重试", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkEditTextValid(etNickname,"昵称不能为空")){
            return;
        }
/*        if(!checkEditTextValid(etSignature,"签名不能为空")){
            return;
        }*/
/*        if(!checkEditTextValid(etSchool,"学校不能为空")){
            return;
        }*/

        //TODO save info to native
        showSavingDialog();
        updateProfile();




    }

    private void updateProfile() {
        final String nickName = etNickname.getText().toString().trim();
        final String signature = etSignature.getText().toString();
        final String school = etSchool.getText().toString();
        EsApiHelper.updateUserProfile(userProfile.getUserId(), nickName, signature, school, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                userProfile.setNickName(nickName);
                userProfile.setUserSgin(signature);
                userProfile.setSchool(school);
                EsUserManager.getInstance().setUserProfile(userProfile);
                ToastUtil.showShort(EsGlobal.getGlobalContext(), "保存成功");
                setResult(Activity.RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showShort(EsGlobal.getGlobalContext(), "保存失败");
            }
        });
    }

    private void showSavingDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("登录中");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    private boolean checkEditTextValid(EditText etContent, String errorTip) {
        if(TextUtils.isEmpty(etContent.getText().toString().trim())){
            Toast.makeText(EsGlobal.getGlobalContext(), errorTip, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initData() {
        userProfile = EsUserManager.getInstance().getUserProfile();
        if(!EsUserManager.getInstance().hasLogIn() || userProfile == null){
            finish();
            return;
        }
        etNickname.setText(userProfile.getNickName());
        etSignature.setText(userProfile.getUserSgin());
        etSchool.setText(userProfile.getSchool());

    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void dismissProgressDialog() {
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
