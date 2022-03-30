package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

import edu.wj.sport.android.bean.UserBean;
import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivityLoginBinding;
import edu.wj.sport.android.utils.HttpUtils;
import edu.wj.sport.android.utils.UserDefault;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {

        UserBean bean = UserDefault.getInstance().getUserInfo();
        if (bean != null){
            mViewBinding.edtPhone.setText(bean.getPhone());
        }

        mViewBinding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 100);
            }
        });

        mViewBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = mViewBinding.edtPhone.getText().toString();
                String pwd = mViewBinding.edtPwd.getText().toString();

                if (phone.isEmpty()){
                    toast("手机号不能为空");
                    return;
                }
                if (pwd.isEmpty()){
                    toast("密码不能为空");
                    return;
                }

                login(phone, pwd);

            }
        });

    }

    private void login(String phone, String pwd) {

        HashMap<String, String> body = new HashMap<>();
        body.put("phone", phone);
        body.put("pwd", pwd);
        HttpUtils.getInstance().post("user/login", body, new HttpCallback(this) {
            @Override
            protected void onData(HttpUtils.ResultBean bean) {
                UserDefault.getInstance().putUserInfo(bean.getEntity(UserBean.class));
                LoginActivity.this.start(MainActivity.class);
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            start(MainActivity.class);
            onBackPressed();
        }
    }
}