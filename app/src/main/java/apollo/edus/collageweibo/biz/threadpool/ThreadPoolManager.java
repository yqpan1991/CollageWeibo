package apollo.edus.collageweibo.biz.threadpool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by panyongqiang on 16/4/8.
 */
public class ThreadPoolManager {
    private Executor preUploadThreadPool;

    private static ThreadPoolManager instance;

    public static ThreadPoolManager getInstance(){
        if(instance == null){
            synchronized (ThreadPoolManager.class){
                if(instance == null){
                    instance = new ThreadPoolManager();
                }
            }
        }
        return instance;
    }

    private ThreadPoolManager(){
        preUploadThreadPool = Executors.newSingleThreadExecutor();
    }

    public Executor getPreUploadThreadPool(){
        return preUploadThreadPool;
    }


}
