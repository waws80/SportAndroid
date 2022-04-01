package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import edu.wj.sport.android.bean.CurriculumBean;
import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivityCurriculumDetailBinding;
import edu.wj.sport.android.utils.GsonUtils;
import edu.wj.sport.android.utils.HttpUtils;

/**
 * 课程详情
 */
public class CurriculumDetailActivity extends BaseActivity<ActivityCurriculumDetailBinding> {

    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {

        String itemStr = getIntent().getStringExtra("item");
        if (itemStr == null || itemStr.isEmpty()){

            toast("非法访问");
            onBackPressed();
            return;
        }

        CurriculumBean bean = GsonUtils.toEntity(CurriculumBean.class, itemStr);

        mViewBinding.videoView.setUp(HttpUtils.RES_URL + "media/" + bean.getVideo(), bean.getCname());
        mViewBinding.videoView.fullscreenButton.setVisibility(View.GONE);
        mViewBinding.videoView.startVideo();

        mViewBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Glide.with(this).load(HttpUtils.RES_URL + "img/" + bean.getThumb()).into(mViewBinding.ivThumb);


        mViewBinding.tvInfo.setText("类型：" + bean.getType()+ "      视频时长：" + bean.getDuration() + "秒\n" + "标签：" + bean.getTip());

        mViewBinding.tvContent.setText(bean.getContent());

    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}