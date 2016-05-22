package apollo.edus.collageweibo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;

/**
 * Created by panyongqiang on 16/5/20.
 */
public class ProfileFragment extends EsBaseFragment implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bt_login).setOnClickListener(this);
    }

    private void userLogin(){
        EsApiHelper.userLogin("15884355963", "123456", new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                Log.e(TAG,"suc:"+string);
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,"error:"+volleyError.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                userLogin();
                break;

        }
    }
}
