package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

import edu.wj.sport.android.bean.UserBean;
import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivityRegisterBinding;
import edu.wj.sport.android.utils.GsonUtils;
import edu.wj.sport.android.utils.HttpUtils;
import edu.wj.sport.android.utils.UserDefault;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    private File imageUri;

    private long birthDayDate = 0L;

    private int sex = 0;

    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {

        mViewBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewBinding.rlAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sysCrop();
            }
        });

        mViewBinding.tvVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewBinding.tvVerifyCode.setEnabled(false);
                new Thread(){

                    int index = 60;

                    @Override
                    public void run() {
                        super.run();

                        while (index > 0){

                            try {
                                Thread.sleep(1000);
                                mViewBinding.tvVerifyCode.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mViewBinding.tvVerifyCode.setText(index + " s ");
                                        index --;
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        mViewBinding.tvVerifyCode.post(new Runnable() {
                            @Override
                            public void run() {
                                mViewBinding.tvVerifyCode.setText("获取验证码");
                                mViewBinding.tvVerifyCode.setEnabled(true);
                            }
                        });

                    }
                }.start();
            }
        });

        mViewBinding.edtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                DatePickerDialog dialog=new DatePickerDialog(mViewBinding.edtBirthday.getContext(), (view, year, month, dayOfMonth) -> {
                    String strDate = year + "-" + month + "-"+dayOfMonth;
                    mViewBinding.edtBirthday.setText(strDate);
                    Date date = new Date(year - 1900, month, dayOfMonth);
                    birthDayDate = date.getTime();

                },calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();

            }
        });


        mViewBinding.rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mViewBinding.man.getId()){
                    sex = 0;
                }else {
                    sex = 1;
                }
            }
        });

        mViewBinding.btnRegister.setOnClickListener(v -> {

            if (this.imageUri == null){
                toast("请选择头像");
                return;
            }

            if (mViewBinding.edtPhone.getText().toString().isEmpty()){
                toast("请输入手机号");
                return;
            }
            if (mViewBinding.edtCode.getText().toString().isEmpty()){
                toast("请输入验证码");
                return;
            }
            String pwd = mViewBinding.edtPwd.getText().toString();
            String pwdRe = mViewBinding.edtPwdRe.getText().toString();
            if (pwd.isEmpty()){
                toast("请输入密码");
                return;
            }

            if (pwdRe.isEmpty()){
                toast("请输入确认密码");
                return;
            }
            if (mViewBinding.edtNickname.getText().toString().isEmpty()){
                toast("请输入昵称");
                return;
            }
            if (birthDayDate == 0){
                toast("请选择生日日期");
                return;
            }
            if (!pwd.equals(pwdRe)){
                toast("两次密码输入不一致");
                return;
            }


            register();

        });

    }



    private void register(){

        HttpUtils.getInstance().uploadAvatar(imageUri, new HttpCallback(this) {
            @Override
            protected void onData(HttpUtils.ResultBean bean) {
                String avatar = bean.getList(String.class).get(0);
                postInfo(avatar);
            }
        });

    }

    private void postInfo(String avatar) {
        String phone = mViewBinding.edtPhone.getText().toString();
        String pwd = mViewBinding.edtPwd.getText().toString();
        String nickname = mViewBinding.edtNickname.getText().toString();

        HashMap<String, String> body = new HashMap<>();
        body.put("phone", phone);
        body.put("nickname", nickname);
        body.put("pwd", pwd);
        body.put("sex", sex + "");
        body.put("birthDate", birthDayDate + "");

        body.put("avatar", avatar);

        HttpUtils.getInstance().post("user/register", body, new HttpCallback(this) {

            @Override
            protected void onData(HttpUtils.ResultBean bean) {
                UserDefault.getInstance().putUserInfo(bean.getEntity(UserBean.class));
                setResult(Activity.RESULT_OK);
                onBackPressed();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == 1){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                imageUri = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM),"avatar.jpeg");
                if (imageUri.exists()){
                    imageUri.delete();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(imageUri));
                Glide.with(this).load(bitmap).circleCrop().into(mViewBinding.ivAvatar);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void sysCrop(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }
}