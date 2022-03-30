package edu.wj.sport.android.ui.fragment;

import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.wj.sport.android.R;
import edu.wj.sport.android.common.BaseFragment;
import edu.wj.sport.android.databinding.FragmentSportBinding;
import edu.wj.sport.android.ui.MainActivity;
import edu.wj.sport.android.utils.BaiduMapLocationHelper;

public class SportFragment extends BaseFragment<FragmentSportBinding> {

    private final BaiduMapLocationHelper helper = new BaiduMapLocationHelper();


    private ScheduledExecutorService schedulerThread;

    long startTime = 0;

    private final List<LatLng> latLngs = new ArrayList<>();

    @Override
    protected void onLoad() {

        helper.bind(this, mViewBinding.mapView, new BaiduMapLocationHelper.OnLocation() {
            @Override
            public void onFirstLocation(BDLocation location) {

            }

            @Override
            public void onLocation(BDLocation location) {

                if (helper.isFlashMapLocation()){

                    latLngs.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    if (latLngs.size() == 1){
                        //标记起始点
                        helper.markStart(location.getLatitude(), location.getLongitude(), R.drawable.map_icon_start);
                    }
                    //绘制路径
                    helper.buildOverlayLine(latLngs, mViewBinding.mapView.getMap());

                    //计算时长
                    exeDuration();
                    //计算速度
                    exeSpeed();
                    //计算总里程
                    exeSum();
                }
            }
        });

        mViewBinding.ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)requireActivity()).changeTabBar();
                helper.setFlashMapLocation(mViewBinding.ivStatus.isChecked());
                if (mViewBinding.ivStatus.isChecked()){
                    schedulerThread = Executors.newSingleThreadScheduledExecutor();
                    startTime = System.currentTimeMillis();
                    latLngs.clear();
                    mViewBinding.mapView.getMap().clear();
                }else {
                    if (latLngs.size() > 1){
                        LatLng latLng = latLngs.get(latLngs.size() - 1);
                        helper.markStart(latLng.latitude, latLng.longitude, R.drawable.map_icon_end);
                    }
                    schedulerThread.shutdown();
                    schedulerThread = null;

                }
            }
        });

        mViewBinding.mapView.getMap().setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ((MainActivity)requireActivity()).changeTabBar();
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });

    }


    /**
     * 计算运动时长
     */
    private void exeDuration() {
        long duration = System.currentTimeMillis() - startTime;
        String durationStr = formatTime(duration / 1000);
        mViewBinding.tvDuration.setText(durationStr);
    }

    /**
     * 计算速度
     */
    private void exeSpeed(){
        if (latLngs.size() > 1){
            LatLng end = latLngs.get(latLngs.size() - 1);
            LatLng second = latLngs.get(latLngs.size() - 2);
            double s = DistanceUtil. getDistance(end, second);
            //一小时运动的距离
            double total = (s * 60 * 60) / 1000.0;
            mViewBinding.tvSpeed.setText(String.format("%.1f", total));
        }
    }

    /**
     * 计算总里程
     */
    private void exeSum(){
        int total = 0;

        LatLng start;
        if (latLngs.size() > 1){
            start = latLngs.get(0);
            for (int i = 1; i < latLngs.size(); i++) {

                LatLng latLng = latLngs.get(i);
                total += DistanceUtil. getDistance(start, latLng);
                start = latLng;
            }
        }

        double t = total / 1000.0;

        mViewBinding.tvTotal.setText(String.format("%.1f", t));
    }


    private String formatTime(long duration) {

        long second = duration % 60;

        long minute = duration / 60;

        long hour = minute / 60;

        Log.d("TAG", "formatTime: " + "duration = " + duration + "   sec = " + second + "   min = " + minute + " hour = " + hour);

        return String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second);

    }
}
