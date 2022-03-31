package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;

import java.util.List;

import edu.wj.sport.android.R;
import edu.wj.sport.android.bean.SportBean;
import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivitySportDetailBinding;
import edu.wj.sport.android.utils.BaiduMapLocationHelper;
import edu.wj.sport.android.utils.DateUtils;
import edu.wj.sport.android.utils.GsonUtils;

public class SportDetailActivity extends BaseActivity<ActivitySportDetailBinding> {

    private final BaiduMapLocationHelper helper = new BaiduMapLocationHelper();

    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {
        String str = getIntent().getStringExtra("item");
        if (str == null || str.isEmpty()){
            onBackPressed();
            toast("非法访问");
            return;
        }


        mViewBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        SportBean sportBean = GsonUtils.toEntity(SportBean.class, str);

        mViewBinding.tvMileage.setText(String.format("总距离 %.1f 千米", sportBean.getMileage()));
        mViewBinding.tvDuration.setText(String.format("总时长 %s", DateUtils.formatTime(sportBean.getDuration())));


        List<LatLng> latLngList = GsonUtils.toList(LatLng.class, sportBean.getPoints());

        LatLng start = latLngList.get(0);
        LatLng end = latLngList.get(latLngList.size() - 1);


        //绘制路径
        helper.buildOverlayLine(latLngList, mViewBinding.mapView.getMap());

        MapStatus.Builder builder = new MapStatus.Builder();


        builder.target(end).zoom(18.0f);
        BaiduMap baiduMap = mViewBinding.mapView.getMap();

        helper.markStart(start.latitude, start.longitude, R.drawable.map_icon_start, baiduMap);
        helper.markStart(end.latitude, end.longitude, R.drawable.map_icon_end, baiduMap);

        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }
}