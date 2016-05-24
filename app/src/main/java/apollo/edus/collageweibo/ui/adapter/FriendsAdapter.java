package apollo.edus.collageweibo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.utils.FillContent;

/**
 * Created by wenmingvs on 16/5/1.
 */
public class FriendsAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<EsUserProfile> mDatas = new ArrayList<>();
    private Context mContext;
    private View mView;


    public FriendsAdapter(ArrayList<EsUserProfile> datas, Context context) {
        this.mContext = context;
        setData(datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.profilefragment_friend_item, parent, false);
        FriendsrViewHolder viewHolder = new FriendsrViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FillContent.fillFriendContent(mContext, mDatas.get(position),
                ((FriendsrViewHolder) holder).friendImg, ((FriendsrViewHolder) holder).friendVerified,
                ((FriendsrViewHolder) holder).follow_me,
                ((FriendsrViewHolder) holder).friendName, ((FriendsrViewHolder) holder).friendContent);

    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        } else {
            return 0;
        }

    }


    public void setData(ArrayList<EsUserProfile> data) {
        this.mDatas.clear();
        if(data != null && !data.isEmpty()){
            this.mDatas.addAll(data);
        }
        notifyDataSetChanged();
    }

    protected class FriendsrViewHolder extends ViewHolder {
        public ImageView friendImg;
        public ImageView friendVerified;
        public ImageView follow_me;
        public TextView friendName;
        public TextView friendContent;

        public FriendsrViewHolder(View view) {
            super(view);
            friendImg = (ImageView) view.findViewById(R.id.friend_img);
            friendVerified = (ImageView) view.findViewById(R.id.friend_verified);
            follow_me = (ImageView) view.findViewById(R.id.follow_me);
            friendName = (TextView) view.findViewById(R.id.friend_name);
            friendContent = (TextView) view.findViewById(R.id.friend_content);

        }
    }
}
