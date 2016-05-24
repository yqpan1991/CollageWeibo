package apollo.edus.collageweibo.ui.fragment;

import android.app.SearchableInfo;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.ui.activity.SearchActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by panyongqiang on 16/5/20.
 */
public class DiscoverFragment extends EsBaseFragment {

    @BindView(R.id.tv_search)
    TextView etSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discoverfragment_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.tv_search)
    public void onClick() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }
}
