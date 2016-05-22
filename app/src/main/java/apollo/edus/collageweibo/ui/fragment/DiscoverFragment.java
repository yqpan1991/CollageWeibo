package apollo.edus.collageweibo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apollo.edus.collageweibo.R;

/**
 * Created by panyongqiang on 16/5/20.
 */
public class DiscoverFragment extends EsBaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discoverfragment_layout, container, false);
    }
    
}
