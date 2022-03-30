package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivitySettingBinding;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {
        mViewBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewBinding.tvEditPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(EditPwdActivity.class);
            }
        });
    }
}