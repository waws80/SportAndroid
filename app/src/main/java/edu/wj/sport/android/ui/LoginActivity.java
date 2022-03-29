package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {

        mViewBinding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(RegisterActivity.class);
            }
        });

    }
}