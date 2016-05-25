package apollo.edus.collageweibo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.utils.FillContent;

/**
 * Created by panyongqiang on 16/5/25.
 */
public class LvFriendsAdapter extends BaseAdapter {

    private List<EsUserProfile> mDataList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;

    public LvFriendsAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<EsUserProfile> dataList){
        mDataList.clear();
        if(dataList != null && !dataList.isEmpty()){
            mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void addData(List<EsUserProfile> dataList){
        if(dataList != null && !dataList.isEmpty()){
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public EsUserProfile getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.profilefragment_friend_item, parent, false);
            convertView.setTag(holder);
            //findViewById
            holder.friendImg = (ImageView) convertView.findViewById(R.id.friend_img);
            holder.friendVerified = (ImageView) convertView.findViewById(R.id.friend_verified);
            holder.follow_me = (ImageView) convertView.findViewById(R.id.follow_me);
            holder.friendName = (TextView) convertView.findViewById(R.id.friend_name);
            holder.friendContent = (TextView) convertView.findViewById(R.id.friend_content);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //update data
        EsUserProfile profile = getItem(position);
        FillContent.fillFriendContent(mContext, profile,
                holder.friendImg, holder.friendVerified, holder.follow_me,
                holder.friendName, holder.friendContent);
        return convertView;
    }

    public static class ViewHolder {
        public ImageView friendImg;
        public ImageView friendVerified;
        public ImageView follow_me;
        public TextView friendName;
        public TextView friendContent;
    }
}
