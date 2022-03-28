package edu.wj.sport.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import edu.wj.sport.android.SportApplication;

public class NetWorkUtils {

    private static final ConnectivityManager connectivityManager = (ConnectivityManager) SportApplication.getApplication()
            .getSystemService(Context.CONNECTIVITY_SERVICE);



    public static boolean isActive(){
        if (connectivityManager == null){
            return false;
        }
        return connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
