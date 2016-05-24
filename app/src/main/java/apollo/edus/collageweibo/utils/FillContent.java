package apollo.edus.collageweibo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cesards.cropimageview.CropImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import apollo.edus.collageweibo.R;
import apollo.edus.collageweibo.biz.bean.WeiboResult;
import apollo.edus.collageweibo.biz.global.EsGlobal;
import apollo.edus.collageweibo.biz.net.api.EsApiHelper;
import apollo.edus.collageweibo.biz.user.EsFavoriteTempManager;
import apollo.edus.collageweibo.biz.user.EsUserManager;
import apollo.edus.collageweibo.biz.user.EsUserProfile;
import apollo.edus.collageweibo.ui.activity.IdeaActivity;
import apollo.edus.collageweibo.ui.activity.LoginActivity;
import apollo.edus.collageweibo.ui.widget.emojitextview.EmojiTextView;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class FillContent {

    private static DisplayImageOptions mAvatorOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.avator_default)
            .showImageForEmptyUri(R.drawable.avator_default)
            .showImageOnFail(R.drawable.avator_default)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new CircleBitmapDisplayer(14671839, 1))
            .build();


    /**
     * 用于加载微博列表图片的配置，进行安全压缩，尽可能的展示图片细节
     */
    private static DisplayImageOptions mImageItemOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.message_image_default)
            .showImageForEmptyUri(R.drawable.message_image_default)
            .showImageOnFail(R.drawable.timeline_image_failure)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .considerExifParams(true)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();


    public static final int IMAGE_TYPE_LONG_TEXT = 1;//长微博
    public static final int IMAGE_TYPE_LONG_PIC = 2;//比较长的微博（但是不至于像长微博那么长）
    public static final int IMAGE_TYPE_WIDTH_PIC = 3;//比较宽的微博
    public static final int IMAGE_TYPE_GIF = 4;

    /**
     * 设置头像的认证icon，记住要手动刷新icon，不然icon会被recycleriview重用，导致显示出错
     *
     * @param user
     * @param profile_img
     * @param profile_verified
     */
    public static void fillProfileImg(final Context context, final EsUserProfile user, final ImageView profile_img, final ImageView profile_verified) {

        profile_verified.setVisibility(View.GONE);
        profile_verified.setVisibility(View.VISIBLE);
        if(user == null){
            ImageLoader.getInstance().displayImage("", profile_img, mAvatorOptions);
        }else{
            ImageLoader.getInstance().displayImage(user.getAvatorUrl(), profile_img, mAvatorOptions);
        }
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra("user", user);
                context.startActivity(intent);*/

            }
        });


    }

    /**
     * 填充顶部微博用户信息数据
     *
     * @param weiboInfo
     * @param profile_img
     * @param profile_name
     * @param profile_time
     * @param weibo_comefrom
     */
    public static void fillTitleBar(Context context, WeiboResult.WeiboInfo weiboInfo, ImageView profile_img, ImageView profile_verified, TextView profile_name, TextView profile_time, TextView weibo_comefrom) {
        fillProfileImg(context, weiboInfo.getUserInfo(), profile_img, profile_verified);
        if(weiboInfo.getUserInfo() == null){
            profile_name.setText("");
        }else{
            profile_name.setText(weiboInfo.getUserInfo().getDisplayName());
        }
        setWeiBoTime(profile_time, weiboInfo.getCreateTime());
//        setWeiBoTime(profile_time, status.created_at);
//        setWeiBoComeFrom(weibo_comefrom, status.source);
    }

/*    public static void setWeiBoTime(TextView textView, String created_at) {
        Date data = DateUtils.parseDate(created_at, DateUtils.WeiBo_ITEM_DATE_FORMAT);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        String time = df.format(data);
        textView.setText(time + "   ");
    }*/

    public static void setWeiBoTime(TextView textView, long created_at) {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        textView.setText(df.format(new Date(created_at)) + "   ");
    }

    public static void setWeiBoComeFrom(TextView textView, String content) {
        if (content != null && content.length() > 0) {
            textView.setText("来自 " + content);
        } else {
            textView.setText("");
        }
    }

    /**
     * 填充微博转发，评论，赞的数量
     *
     * @param weiboInfo
     * @param bottombar_retweet
     * @param bottombar_comment
     * @param bottombar_attitude
     * @param comment
     * @param redirect
     * @param feedlike
     */
    public static void fillButtonBar(final Context context, final WeiboResult.WeiboInfo weiboInfo, LinearLayout bottombar_retweet, final LinearLayout bottombar_comment, final LinearLayout bottombar_attitude, TextView comment, TextView redirect, final TextView feedlike) {
        comment.setText(weiboInfo.getMreply() + "");
        redirect.setText(weiboInfo.getMcopy() + "");
        feedlike.setText(weiboInfo.getMfay() + "");


        bottombar_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断图文微博
/*                if (status.retweeted_status == null) {
                    Intent intent = new Intent(context, OriginPicTextCommentActivity.class);
                    intent.putExtra("weiboitem", status);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, RetweetPicTextCommentActivity.class);
                    intent.putExtra("weiboitem", status);
                    context.startActivity(intent);
                }*/

            }
        });


        bottombar_retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IdeaActivity.class);
                intent.putExtra("status", weiboInfo);
                context.startActivity(intent);
            }
        });
        if(EsFavoriteTempManager.getInstance().getFavoriteWeiboListSnapShot().contains(weiboInfo.getWeiboId())){
            bottombar_attitude.setSelected(true);
        }else{
            bottombar_attitude.setSelected(false);
        }
        bottombar_attitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(!preCheckValid(v)){
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setMessage("处理中...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                final boolean isFavorite = EsFavoriteTempManager.getInstance().getFavoriteWeiboListSnapShot().contains(weiboInfo.getWeiboId()) ? false : true;
                EsApiHelper.favoriteWeibo(isFavorite , weiboInfo.getWeiboId(), new Response.Listener<String>(){

                    @Override
                    public void onResponse(String string) {
                        progressDialog.dismiss();
                        if(isFavorite){
                            bottombar_attitude.setSelected(true);
                            weiboInfo.setMfay(weiboInfo.getMfay()+1);
                            feedlike.setText(weiboInfo.getMfay() + "");
                            EsFavoriteTempManager.getInstance().addFavorite(weiboInfo.getWeiboId());
                        }else{
                            bottombar_attitude.setSelected(false);
                            weiboInfo.setMfay(weiboInfo.getMfay()-1);
                            EsFavoriteTempManager.getInstance().delFavorite(weiboInfo.getWeiboId());
                        }
                        feedlike.setText(weiboInfo.getMfay() + "");
                        ToastUtil.showShort(EsGlobal.getGlobalContext(), isFavorite ? "点赞成功" : "取消点赞成功");
                    }
                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.showShort(EsGlobal.getGlobalContext(), isFavorite ? "点赞失败" : "取消点赞失败");
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    private static boolean preCheckValid(final View v) {
        if(!EsUserManager.getInstance().hasLogIn()){
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("此功能需要登录,是否去登录?")
                    .setCancelable(true)
                    .setIcon(R.drawable.logo)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            v.getContext().startActivity(new Intent(v.getContext().getApplicationContext(), LoginActivity.class));
                            if(v.getContext() instanceof Activity){
                                ((Activity)v.getContext()).finish();
                            }
                        }
                    })
                    .setNegativeButton("取消", null);
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
        if(!NetworkUtils.isNetworkAvailable(v.getContext())){
            ToastUtil.showShort(v.getContext(), "未连接网络，请稍后重试");
            return false;
        }
        return true;
    }


    /**
     * 决定是否隐藏转发，评论，赞的底部的bar，进入weibodetail的时候隐藏他
     *
     * @param visible
     * @param layout
     */
    public static void showButtonBar(int visible, LinearLayout layout) {
        if (visible == View.VISIBLE) {
            layout.setVisibility(View.VISIBLE);
        } else if (visible == View.GONE) {
            layout.setVisibility(View.GONE);
        } else if (visible == View.INVISIBLE) {
            layout.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 填充微博文字内容
     */
    public static void fillWeiBoContent(String text, Context context, EmojiTextView weibo_content) {
        weibo_content.setText(WeiBoContentTextUtil.getWeiBoContent(text, context, weibo_content));
        weibo_content.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 填充微博图片列表,包括原创微博和转发微博中的图片都可以使用
     */
    public static void fillWeiBoImgList(WeiboResult.WeiboInfo weiboInfo, Context context, RecyclerView imageList) {
        imageList.setVisibility(View.GONE);
        imageList.setVisibility(View.VISIBLE);
//        ArrayList<String> imageDatas = status.origin_pic_urls;
        ArrayList<String> imageDatas = new ArrayList<>();
/*        GridLayoutManager gridLayoutManager = initGridLayoutManager(imageDatas, context);
        ImageAdapter imageAdapter = new ImageAdapter(imageDatas, context);
        imageList.setHasFixedSize(true);
        imageList.setAdapter(imageAdapter);
        imageList.setLayoutManager(gridLayoutManager);
        imageAdapter.setData(imageDatas);*/
        if (imageDatas == null || imageDatas.size() == 0) {
            imageList.setVisibility(View.GONE);
        }
    }

    /**
     * 根据图片数量，初始化GridLayoutManager，并且设置列数，
     * 当图片 = 1 的时候，显示1列
     * 当图片<=4张的时候，显示2列
     * 当图片>4 张的时候，显示3列
     *
     * @return
     */
    private static GridLayoutManager initGridLayoutManager(ArrayList<String> imageDatas, Context context) {
        GridLayoutManager gridLayoutManager;
        if (imageDatas != null) {
            switch (imageDatas.size()) {
                case 1:
                    gridLayoutManager = new GridLayoutManager(context, 1);
                    break;
                case 2:
                    gridLayoutManager = new GridLayoutManager(context, 2);
                    break;
                case 3:
                    gridLayoutManager = new GridLayoutManager(context, 3);
                    break;
                case 4:
                    gridLayoutManager = new GridLayoutManager(context, 2);
                    break;
                default:
                    gridLayoutManager = new GridLayoutManager(context, 3);
                    break;
            }
        } else {
            gridLayoutManager = new GridLayoutManager(context, 3);
        }
        return gridLayoutManager;
    }

    /**
     * 转发的文字
     */
    public static void fillRetweetContent(WeiboResult.WeiboInfo weiboInfo, Context context, TextView origin_nameAndcontent) {
/*        if (status.retweeted_status.user != null) {
            StringBuffer retweetcontent_buffer = new StringBuffer();
            retweetcontent_buffer.setLength(0);
            retweetcontent_buffer.append("@");
            retweetcontent_buffer.append(status.retweeted_status.user.name + " :  ");
            retweetcontent_buffer.append(status.retweeted_status.text);
            origin_nameAndcontent.setText(WeiBoContentTextUtil.getWeiBoContent(retweetcontent_buffer.toString(), context, origin_nameAndcontent));
        } else {
            origin_nameAndcontent.setText("抱歉，此微博已被作者删除。查看帮助：#网页链接#");
        }*/

    }

    /**
     * 填充微博列表图片
     *
     * @param context
     * @param datas
     * @param position
     * @param imageView
     */
    public static void fillImageList(final Context context, final ArrayList<String> datas, final int position, final ImageView imageView, final ImageView imageType) {
        ImageLoader.getInstance().displayImage(datas.get(position), imageView, mImageItemOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

                //单张图片的时候，从预设的3种图片尺寸中随机选一种
                if (datas.size() == 1) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                    Random random = new Random();
                    //int randomnum = random.nextInt(0);
                    int randomnum = 1;
                    //在0，1,2中随机取一个数
                    if (randomnum == 0) {
                        //竖直方向的长方形尺寸
                        layoutParams.height = (int) context.getResources().getDimension(R.dimen.home_weiboitem_imagesize_vertical_rectangle_height);
                        layoutParams.width = (int) context.getResources().getDimension(R.dimen.home_weiboitem_imagesize_vertical_rectangle_width);
                    } else if (randomnum == 1) {
                        //水平方向的长方形尺寸
                        layoutParams.height = (int) context.getResources().getDimension(R.dimen.home_weiboitem_imagesize_horizontal_rectangle_height);
                        layoutParams.width = (int) context.getResources().getDimension(R.dimen.home_weiboitem_imagesize_horizontal_rectangle_width);
                    } else {
                        //正方形尺寸
                        layoutParams.height = (int) context.getResources().getDimension(R.dimen.home_weiboitem_imagesize_square_height);
                        layoutParams.width = (int) context.getResources().getDimension(R.dimen.home_weiboitem_imagesize_square_width);
                    }
                }

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                //根据加载完成的BitMap大小，判断是否是长微博图片，设置右下角的图片类型icon
                int type = returnImageType(context, bitmap);
                if (type == IMAGE_TYPE_LONG_TEXT) {
                    imageType.setImageResource(R.drawable.timeline_image_longimage);
                }

                //根据请求的URL，判断是否是Gif，设置右下角的图片类型icon
                if (returnImageType(context, datas.get(position)) == IMAGE_TYPE_GIF) {
                    imageType.setImageResource(R.drawable.timeline_image_gif);
                }

                //若是长微博
                if (returnImageType(context, bitmap) == IMAGE_TYPE_LONG_TEXT) {
                    ((CropImageView) imageView).setCropType(CropImageView.CropType.CENTER_TOP);
                }

                //若是长微博，还需要纠正尺寸
                if (returnImageType(context, bitmap) == IMAGE_TYPE_LONG_TEXT && datas.size() == 1) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                    layoutParams.height = (int) context.getResources().getDimension(R.dimen.home_weiboitem_imagesize_vertical_rectangle_height);
                    layoutParams.width = (int) context.getResources().getDimension(R.dimen.home_weiboitem_imagesize_vertical_rectangle_width);
                    ((CropImageView) imageView).setCropType(CropImageView.CropType.CENTER_TOP);
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Intent intent = new Intent(context, ImageDetailsActivity.class);
                intent.putExtra("imagelist_url", datas);
                intent.putExtra("image_position", position);
                context.startActivity(intent);*/
            }
        });
    }


    /**
     * 根据下载的图片的大小，返回图片的类型
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static int returnImageType(Context context, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //长微博尺寸
        if (height >= width * 3) {
            return IMAGE_TYPE_LONG_TEXT;
        }

        return IMAGE_TYPE_WIDTH_PIC;
    }

    /**
     * 根据url链接判断是否为Gif，返回图片类型
     *
     * @param context
     * @param url
     * @return
     */
    public static int returnImageType(Context context, String url) {
        //长微博尺寸
        if (url.endsWith(".gif")) {
            return IMAGE_TYPE_GIF;
        }

        return IMAGE_TYPE_WIDTH_PIC;
    }


    public static void FillDetailBar(int comments_count, int reposts_count, int attitudes_count, TextView comment, TextView redirect, TextView feedlike) {
        comment.setText("评论 " + comments_count);
        redirect.setText("转发 " + reposts_count);
        feedlike.setText("赞 " + attitudes_count);
    }

    public static void RefreshNoneView(Context context, int comments_count, View noneView) {


        if (NetUtil.isConnected(context)) {
            if (comments_count > 0) {
                noneView.setVisibility(View.GONE);
            } else if (comments_count == 0) {
                noneView.setVisibility(View.VISIBLE);
            }
        } else {
            if (!NewFeature.CACHE_DETAIL_ACTIVITY) {
                noneView.setVisibility(View.VISIBLE);
                TextView textView = (TextView) noneView.findViewById(R.id.tv_normal_refresh_footer_status);
                textView.setText("网络出错啦");
            }
        }

    }


    /**
     * 初始化用于显示评论，转发，赞的viewpager
     *
     * @param context
     * @param commentCount
     * @param commentArrayList
     * @param recyclerView
     * @param commentView
     */

/*    public static void FillCommentList(Context context, int commentCount, ArrayList<Comment> commentArrayList, final RecyclerView recyclerView, TextView commentView) {
        CommentAdapter commentAdapter = new CommentAdapter(context, commentArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commentAdapter);
    }*/

    public static void FillCenterContent(WeiboResult.WeiboInfo retweetstatus, ImageView profile_img, TextView profile_name, TextView content) {
        if (retweetstatus != null /*&& retweetstatus.user != null*/) {
/*            if (retweetstatus.origin_pic_urls == null || retweetstatus.origin_pic_urls.size() == 0) {
                ImageLoader.getInstance().displayImage(retweetstatus.user.avatar_hd, profile_img);
            } else {
                ImageLoader.getInstance().displayImage(retweetstatus.origin_pic_urls.get(0), profile_img);
            }*/
            profile_name.setText(retweetstatus.getNickname());
            content.setText(retweetstatus.getContent());
        } else {
            profile_img.setImageResource(R.drawable.photo_filter_image_empty);
            content.setText("抱歉，此微博已被作者删除。查看帮助：#网页链接#");
        }
    }


    /**
     * 填充顶部微博用户信息数据
     *
     * @param comment
     * @param profile_img
     * @param profile_verified
     * @param profile_name
     * @param profile_time
     * @param weibo_comefrom
     */
/*    public static void fillTitleBar(Context context, Comment comment, ImageView profile_img, ImageView profile_verified, TextView profile_name, TextView profile_time, TextView weibo_comefrom) {
        FillContent.fillProfileImg(context, comment.user, profile_img, profile_verified);
        profile_name.setText(comment.user.name);
        FillContent.setWeiBoTime(profile_time, comment.created_at);
        FillContent.setWeiBoComeFrom(weibo_comefrom, comment.source);
    }*/

    /**
     * 填充粉丝的内容
     *
     * @param user
     * @param followerImg
     * @param followerVerf
     * @param followerName
     * @param content
     * @param profileComefrom
     * @param follwerRelation
     */
   /* public static void fillFollowContent(Context context, User user,
                                         ImageView followerImg, ImageView followerVerf,
                                         TextView followerName, TextView content,
                                         TextView profileComefrom, ImageView follwerRelation) {


        FillContent.fillProfileImg(context, user, followerImg, followerVerf);
        followerName.setText(user.name);
        if (user.status != null) {//有些人不发微博
            content.setText(user.status.text);
            profileComefrom.setText(user.status.source);
        }
        if (user.following == true) {
            follwerRelation.setImageResource(R.drawable.card_icon_arrow);
        } else {
            follwerRelation.setImageResource(R.drawable.card_icon_addattention);
        }
    }*/

/*    public static void fillFriendContent(Context context, User user, ImageView friendImg, ImageView friendVerified, ImageView followme, TextView friendName, TextView friendContent) {
        FillContent.fillProfileImg(context, user, friendImg, friendVerified);
        if (user.follow_me) {
            followme.setVisibility(View.VISIBLE);
        } else {
            followme.setVisibility(View.INVISIBLE);
        }

        friendName.setText(user.name);
        if (user.status != null) {//有些人不发微博
            friendContent.setText(user.status.text);
        }
    }*/

/*
    public static void fillUserHeadView(Context context, final User user, final ImageView userCoverimg, ImageView userImg, ImageView userVerified, TextView userName,
                                        ImageView userSex, TextView userFriends,
                                        TextView userFollows, TextView userVerifiedreason) {
        if (user.cover_image_phone != null && user.cover_image_phone.length() > 0) {
            ImageLoader.getInstance().displayImage(user.cover_image_phone, userCoverimg, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if (user.cover_image != null && user.cover_image.length() > 0) {
                        ImageLoader.getInstance().displayImage(user.cover_image, userCoverimg);
                    } else {
                        userCoverimg.setImageResource(R.drawable.cover_image);
                    }
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        } else if (user.cover_image != null && user.cover_image.length() > 0) {
            ImageLoader.getInstance().displayImage(user.cover_image, userCoverimg);
        } else {
            userCoverimg.setImageResource(R.drawable.cover_image);
        }

        userCoverimg.setColorFilter(Color.parseColor("#1e000000"));

        FillContent.fillProfileImg(context, user, userImg, userVerified);
        userName.setText(user.name);

        if (user.gender.equals("m")) {
            userSex.setImageResource(R.drawable.userinfo_icon_male);
        } else if (user.gender.equals("f")) {
            userSex.setImageResource(R.drawable.userinfo_icon_female);
        } else {
            userSex.setVisibility(View.GONE);
        }
        userFriends.setText("关注  " + user.friends_count);
        userFollows.setText("粉丝  " + user.followers_count);
        if (!user.verified) {
            userVerifiedreason.setVisibility(View.GONE);
        } else {
            userVerifiedreason.setText("微博认证：" + user.verified_reason);
        }
    }*/


}
