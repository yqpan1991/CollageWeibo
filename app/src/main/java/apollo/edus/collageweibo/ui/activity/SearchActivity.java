package apollo.edus.collageweibo.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import apollo.edus.collageweibo.R;

/**
 * Created by panyongqiang on 16/5/24.
 */
public class SearchActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_contact);

        //search --> result
        //click show user detail
        //detail show relationShip
        //this is OK
        //界面上不提供操作，只有点击进入详情后，才能添加删除关注的操作
    }

}
