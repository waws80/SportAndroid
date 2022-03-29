package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;

import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivityRegisterBinding;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    private Uri imageUri;

    private File avatarFile;

    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {

        imageUri = Uri.fromFile(new File(getExternalFilesDir(Environment.DIRECTORY_DCIM),"avatar.jpeg"));

        mViewBinding.rlAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sysCrop();
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
                mViewBinding.ivAvatar.setImageBitmap(bitmap);
                avatarFile = new File(imageUri.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void sysCrop(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 100);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        //intent.putExtra("output", imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        startActivityForResult(intent, 1);
    }
}