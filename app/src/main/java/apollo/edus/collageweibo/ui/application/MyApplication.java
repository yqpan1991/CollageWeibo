package apollo.edus.collageweibo.ui.application;

import android.app.Application;
import android.content.Context;

import com.baidu.location.service.LocationService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import apollo.edus.collageweibo.biz.global.EsGlobal;

/**
 * Created by panyongqiang on 16/5/20.
 */
public class MyApplication extends Application {

    public LocationService locationService;

    private void initImageLoader(Context context){
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EsGlobal.setGlobalContext(this);
        initImageLoader(getApplicationContext());
        Fresco.initialize(getApplicationContext());
        initBaiduLocation();
    }

    private void initBaiduLocation() {
        locationService = new LocationService(getApplicationContext());
    }
}
