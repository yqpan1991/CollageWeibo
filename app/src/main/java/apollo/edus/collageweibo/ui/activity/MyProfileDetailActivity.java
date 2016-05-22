package apollo.edus.collageweibo.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import apollo.edus.collageweibo.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Panda on 2016/5/22.
 */
public class MyProfileDetailActivity extends Activity {

    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.loadingView)
    RelativeLayout loadingView;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.et_signature)
    EditText etSignature;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.sp_gender)
    Spinner spGender;
    @BindView(R.id.tv_birth)
    TextView tvBirth;
    @BindView(R.id.dp_birth)
    DatePicker dpBirth;
    @BindView(R.id.tv_school)
    TextView tvSchool;
    @BindView(R.id.et_school)
    EditText etSchool;
    @BindView(R.id.tv_went_time)
    TextView tvWentTime;
    @BindView(R.id.dp_went_time)
    DatePicker dpWentTime;

    @BindView(R.id.tv_save)
    DatePicker tvSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile_detail_layout);
        ButterKnife.bind(this);


    }

}
