package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivityEditPwdBinding;
import edu.wj.sport.android.utils.HttpUtils;

public class EditPwdActivity extends BaseActivity<ActivityEditPwdBinding> {

    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {
        mViewBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }


    private void submit(){

        String oldPwd = mViewBinding.edtPwdOld.getText().toString();

        String newPwd = mViewBinding.edtPwd.getText().toString();

        String newPwdRe = mViewBinding.edtPwdRe.getText().toString();


        if (oldPwd.isEmpty() || newPwd.isEmpty() || newPwdRe.isEmpty()){
            toast("请输入修改信息");
            return;
        }

        if (!newPwd.equals(newPwdRe)){
            toast("两次输入的新密码不一致");
            return;
        }

        HashMap<String, String> body = new HashMap<>();
        body.put("oldPwd", oldPwd);
        body.put("pwd", newPwd);
        HttpUtils.getInstance().put("user/info", body, new HttpCallback(this) {
            @Override
            protected void onData(HttpUtils.ResultBean bean) {
                toast("修改密码成功");
                onBackPressed();
            }
        });


    }
}