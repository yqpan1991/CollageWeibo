package apollo.edus.collageweibo.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.ui.activity.LoginActivity;
import apollo.edus.collageweibo.ui.activity.RelativeWeiboListActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by panyongqiang on 16/5/20.
 */
public class MessageFragment extends EsBaseFragment {


    @BindView(R.id.mention_layout)
    RelativeLayout mentionLayout;
    @BindView(R.id.comment_layout)
    RelativeLayout commentLayout;
    @BindView(R.id.attitude_layout)
    RelativeLayout attitudeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messagefragment_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @OnClick({R.id.mention_layout, R.id.comment_layout, R.id.attitude_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mention_layout:
                startRelativeActivity(3);
                break;
            case R.id.comment_layout:
                startRelativeActivity(1);
                break;
            case R.id.attitude_layout:
                startRelativeActivity(2);
                break;
        }
    }

    private void startRelativeActivity(int type) {
        if(!EsUserManager.getInstance().hasLogIn()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("此功能需要登录,是否去登录?")
                    .setCancelable(true)
                    .setIcon(R.drawable.logo)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getContext().startActivity(new Intent(getContext().getApplicationContext(), LoginActivity.class));
                        }
                    })
                    .setNegativeButton("取消", null);
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            Intent intent =  new Intent(getActivity(), RelativeWeiboListActivity.class);
            intent.putExtra(RelativeWeiboListActivity.EXTRA_TYPE,type);
            startActivity(intent);
        }
    }
}
