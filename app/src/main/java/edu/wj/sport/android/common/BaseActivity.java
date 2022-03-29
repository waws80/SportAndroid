package edu.wj.sport.android.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewbinding.ViewBinding;


import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import edu.wj.sport.android.R;
import edu.wj.sport.android.ui.LoginActivity;
import edu.wj.sport.android.ui.RegisterActivity;
import edu.wj.sport.android.utils.HttpUtils;
import edu.wj.sport.android.utils.UserDefault;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    protected VB mViewBinding;

    private AlertDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (!UserDefault.getInstance().isLogin() && getClass() != LoginActivity.class && getClass() != RegisterActivity.class){
            start(LoginActivity.class);
            onBackPressed();
            return;
        }
        try {
            Class<?> vbClass = (Class<?>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Method method = vbClass.getDeclaredMethod("inflate", LayoutInflater.class);
            mViewBinding = (VB) method.invoke(null, getLayoutInflater());
            setContentView(mViewBinding.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressDialog = new AlertDialog.Builder(this)
                .setView(R.layout.progress_layout)
                .create();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        onLoad(savedInstanceState);

    }


    protected void showProgress(){
        if (progressDialog.isShowing()){
            return;
        }
        progressDialog.show();
    }

    protected void hideProgress(){
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    protected abstract void onLoad(@Nullable Bundle savedInstanceState);
    
    
    protected void start(Class<?> activityClass){
        start(activityClass, Bundle.EMPTY);
    }
    
    
    protected void start(Class<?> activityClass, Bundle bundle){
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    protected void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }






    protected abstract class HttpCallback extends HttpUtils.DataCallback{

        protected HttpCallback(LifecycleOwner owner) {
            super(owner);
        }

        @Override
        protected void start() {
            super.start();
            showProgress();
        }

        @Override
        protected void complete() {
            super.complete();
            hideProgress();
        }

        @Override
        protected void onError(HttpUtils.ResultBean bean) {

        }
    }
}
