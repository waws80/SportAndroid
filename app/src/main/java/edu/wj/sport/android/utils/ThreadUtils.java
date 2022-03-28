package edu.wj.sport.android.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.core.os.HandlerCompat;


public class ThreadUtils {

    private static final Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    private static final HandlerThread handlerThread = new HandlerThread("handler-thread");

    private static final Handler threadHandler;

    static {
        handlerThread.start();
        threadHandler = HandlerCompat.createAsync(handlerThread.getLooper());
    }


    public static void runMain(Runnable runnable){
        mainHandler.post(runnable);
    }

    public static void runThread(Runnable runnable){
        threadHandler.post(runnable);
    }
}
