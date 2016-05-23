package apollo.edus.collageweibo.utils;

/**
 * Created by wenmingvs on 2016/1/6.
 */
public class NewFeature {


    //每次刷新微博获取的数量
    public static int GET_WEIBO_NUMS = 30;

    //滑动到底部，获取评论的数量
    public static int LOADMORE_WEIBO_ITEM = 20;

    //每次刷新微博，获取评论的数量
    public static int GET_COMMENT_ITEM = 30;

    //滑动到底部，获取评论的数量
    public static int LOADMORE_COMMENT_ITEM = 20;

    //每次刷新微博，获取转发的数量
    public static int GET_RETWEET_ITEM = 20;

    //滑动到底部，获取转发的数量
    public static int LOADMORE_RETWEET_ITEM = 10;

    //每次刷新，获取@的数量
    public static int GET_MENTION_ITEM = 20;

    //滑动到底部，获取@的数量
    public static int LOADMORE_MENTION_ITEM = 10;

    //每次刷新，获取公共微博的数量
    public static int GET_PUBLICWEIBO_NUMS = 30;

    //滑动到底部，获取公共微博的数量
    public static int LOAD_PUBLICWEIBO_ITEM = 20;

    //通用设置
    public static boolean HAVA_IMAGE = true;

    public static int TEXT_SIZE_BIG = 1;
    public static int TEXT_SIZE_MIDDLE = 2;
    public static int TEXT_SIZE_SMAILL = 3;

    public static int GET_IMG_QUANTITY_HIGT = 1;
    public static int GET_IMG_QUANTITY_NORMAL = 2;

    public static int SEND_IMG_QUANTITY_HIGT = 1;
    public static int SEND_IMG_QUANTITY_NORMAL = 2;


    //缓存微博数据
    public static boolean CACHE_WEIBOLIST = false;

    //缓存单条微博的评论数据
    public static boolean CACHE_DETAIL_ACTIVITY = false;

    //缓存message模块的@数据
    public static boolean CACHE_MESSAGE_MENTION = false;

    //缓存message模块的评论数据
    public static boolean CACHE_MESSAGE_COMMENT = false;


}
