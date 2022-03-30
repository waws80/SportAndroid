package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import java.sql.Date;
import java.text.SimpleDateFormat;

import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivityUserInfoBinding;
import edu.wj.sport.android.utils.HttpUtils;
import edu.wj.sport.android.utils.UserDefault;

public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding> {

    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {

        String avatarUrl = HttpUtils.IMG_URL + "avatar/" + UserDefault.getInstance().getUserInfo().getAvatar();

        String nickName = UserDefault.getInstance().getUserInfo().getNickname();

        String phone = UserDefault.getInstance().getUserInfo().getPhone();

        Date  birthDay = new Date(UserDefault.getInstance().getUserInfo().getBirthDate());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");

        String sex = UserDefault.getInstance().getUserInfo().getSexStr();


        Glide.with(this).load(avatarUrl).circleCrop().into(mViewBinding.ivAvatar);

        mViewBinding.tvNickname.setText(nickName);
        mViewBinding.tvPhone.setText(phone);
        mViewBinding.tvBirthday.setText(dateFormat.format(birthDay));
        mViewBinding.tvSex.setText(sex);

        mViewBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}