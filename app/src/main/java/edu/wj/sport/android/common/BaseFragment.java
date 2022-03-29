package edu.wj.sport.android.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewbinding.ViewBinding;


import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.wj.sport.android.R;
import edu.wj.sport.android.ui.LoginActivity;
import edu.wj.sport.android.utils.HttpUtils;

public abstract class BaseFragment<VB extends ViewBinding> extends Fragment {

    protected VB mViewBinding;

    private AlertDialog progressDialog;

    private final AtomicBoolean load = new AtomicBoolean(false);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            Class<?> vbClass = (Class<?>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Method method = vbClass.getDeclaredMethod("inflate", LayoutInflater.class);
            mViewBinding = (VB) method.invoke(null, getLayoutInflater());
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressDialog = new AlertDialog.Builder(requireContext())
                .setView(R.layout.progress_layout)
                .create();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        return mViewBinding.getRoot();
    }


    protected boolean openLazy(){
        return true;
    }

    protected abstract void onLoad();

    @Override
    public void onResume() {
        super.onResume();
        if (openLazy() && !load.get()){
            load.set(true);
            onLoad();
            return;
        }
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

    protected void start(Class<?> activityClass){
        start(activityClass, Bundle.EMPTY);
    }


    protected void start(Class<?> activityClass, Bundle bundle){
        Intent intent = new Intent(requireActivity(), activityClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    protected void toast(String msg){
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
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
            bean.toast();
            if (bean.getCode() == 401 || bean.getCode() == 403){
                startActivity(new Intent(getContext(), LoginActivity.class));
                requireActivity().onBackPressed();
                if (bean.getCode() == 403){
                    toast("当前账号被封禁");
                }else {
                    toast("当前账号未登录");
                }
            }
        }
    }
}
