package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import edu.wj.sport.android.bean.UserBean;
import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivityEditInfoBinding;
import edu.wj.sport.android.utils.HttpUtils;
import edu.wj.sport.android.utils.UserDefault;

public class EditInfoActivity extends BaseActivity<ActivityEditInfoBinding> {

    private File imageUri;


    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {
        String avatarUrl = HttpUtils.RES_URL + "avatar/" + UserDefault.getInstance().getUserInfo().getAvatar();

        String nickName = UserDefault.getInstance().getUserInfo().getNickname();

        Glide.with(this)
                .load(avatarUrl)
                .circleCrop()
                .into(mViewBinding.ivAvatar);

        mViewBinding.edtNickname.setText(nickName);


        mViewBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewBinding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sysCrop();
            }
        });

        mViewBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null && mViewBinding.edtNickname.getText().toString().equals(UserDefault.getInstance().getUserInfo().getNickname())){
                    toast("没有修改");
                    return;
                }
                submit();

            }
        });
    }

    private void submit() {
        if (imageUri != null){

            HttpUtils.getInstance().uploadAvatar(imageUri, new HttpCallback(this) {
                @Override
                protected void onData(HttpUtils.ResultBean bean) {
                    String avatar = bean.getList(String.class).get(0);
                    submitInfo(avatar);
                }
            });
        }else {
            submitInfo("");
        }
    }

    private void submitInfo(String avatar){
        HashMap<String, String> body = new HashMap<>();
        body.put("avatar", avatar);
        String nickname = mViewBinding.edtNickname.getText().toString();
        if (!nickname.equals(UserDefault.getInstance().getUserInfo().getNickname())){
            body.put("nickname", nickname);
        }
        HttpUtils.getInstance().put("user/info", body, new HttpCallback(this) {
            @Override
            protected void onData(HttpUtils.ResultBean bean) {
                UserDefault.getInstance().putUserInfo(bean.getEntity(UserBean.class));
                toast("修改成功");
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