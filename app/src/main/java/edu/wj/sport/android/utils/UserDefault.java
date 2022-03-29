package edu.wj.sport.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import edu.wj.sport.android.SportApplication;
import edu.wj.sport.android.bean.UserBean;


public class UserDefault {

    private final SharedPreferences sp;

    private UserDefault(){
        sp = SportApplication.getApplication().getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    private static class Builder{
        private static final UserDefault USER_DEFAULT = new UserDefault();
    }

    public static UserDefault getInstance(){
        return Builder.USER_DEFAULT;
    }

    public boolean isLogin(){
        return sp.getBoolean("isLogin", false);
    }


    public void putUserInfo(UserBean info){
        sp.edit().putString("info", GsonUtils.toJson(info)).apply();
        sp.edit().putBoolean("isLogin", true).apply();
    }


    @Nullable
    public UserBean getUserInfo(){
        String string = sp.getString("info", "");
        if (string.isEmpty()){
            return null;
        }
        return GsonUtils.toEntity(UserBean.class, string);
    }


    public String getUserId(){
        if (getUserInfo() == null){
            return "";
        }
        return getUserInfo().getId();
    }

    public void clear(){
        sp.edit().putBoolean("isLogin", false).apply();
    }


}
