package apollo.edus.collageweibo.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Panda on 2016/5/22.
 */
public class SettingActivity extends Activity {

    @BindView(R.id.btn_logout)
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        if (!EsUserManager.getInstance().hasLogIn()) {
            finish();
            return;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btn_logout)
    public void onClick() {
        EsApiHelper.userLogout();
        EsUserManager.getInstance().setHasLogin(false);
        EsUserManager.getInstance().clearNativeSaveUserInfo();
        EsUserManager.getInstance().clearNativeUserProfile();
        EsUserManager.getInstance().notifyUserLogout();
        finish();
    }
}
