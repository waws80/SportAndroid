package edu.wj.sport.android.utils;

import androidx.annotation.MainThread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TimeDuration {


    private final long startTime = System.currentTimeMillis();

    private final AtomicBoolean executing = new AtomicBoolean(true);

    private final AtomicLong lastPauseMills = new AtomicLong(0);

    private final AtomicLong pauseMills = new AtomicLong(0);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private final AtomicLong duration = new AtomicLong(0L);

    public void resume(){
        executing.set(true);
        if (lastPauseMills.get() == 0){
            return;
        }
        long offset = System.currentTimeMillis() - lastPauseMills.get();
        pauseMills.set(pauseMills.get() + offset );
    }


    public void pause(){
        executing.set(false);
        lastPauseMills.set(System.currentTimeMillis());
    }


    public void stop(){
        scheduledExecutorService.shutdownNow();
        executing.set(false);
    }


    public long getDuration(){
        return duration.get();
    }


    public void executing(OnDateDurationCallback callback){
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (executing.get()){
                long len = System.currentTimeMillis() - startTime - pauseMills.get();
                duration.set(len);
                ThreadUtils.runMain(() -> callback.onNext(len));
            }
        }, 1, 1, TimeUnit.SECONDS);
    }



    public interface OnDateDurationCallback{

        @MainThread
        void onNext(long duration);
    }


}
