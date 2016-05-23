package apollo.edus.collageweibo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import apollo.edus.collageweibo.R;


/**
 * Created by wenmingvs on 16/4/27.
 */
public class SeachHeadView extends RelativeLayout {

    public SeachHeadView(Context context) {
        super(context);
        init(context);
    }

    public SeachHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SeachHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.headsearchview, this);
    }
}
