package edu.wj.sport.android.ui.fragment;

import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.wj.sport.android.R;
import edu.wj.sport.android.common.BaseFragment;
import edu.wj.sport.android.databinding.FragmentSportBinding;
import edu.wj.sport.android.ui.MainActivity;
import edu.wj.sport.android.ui.SportHistoryActivity;
import edu.wj.sport.android.utils.BaiduMapLocationHelper;
import edu.wj.sport.android.utils.DateUtils;
import edu.wj.sport.android.utils.GsonUtils;
import edu.wj.sport.android.utils.HttpUtils;

public class SportFragment extends BaseFragment<FragmentSportBinding> {

    private final BaiduMapLocationHelper helper = new BaiduMapLocationHelper();

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

                if (helper.isFlashMapLocation()) {

                    latLngs.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    if (latLngs.size() == 1) {
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
                ((MainActivity) requireActivity()).changeTabBar(mViewBinding.ivStatus.isChecked());
                helper.setFlashMapLocation(mViewBinding.ivStatus.isChecked());
                if (mViewBinding.ivStatus.isChecked()) {
                    startTime = System.currentTimeMillis();
                } else {
                    if (latLngs.size() > 1) {
                        LatLng latLng = latLngs.get(latLngs.size() - 1);
                        helper.markStart(latLng.latitude, latLng.longitude, R.drawable.map_icon_end);
                    }
                    //结束运动上传运动数据
                    startUploadSportData();

                }
            }
        });


        mViewBinding.tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(SportHistoryActivity.class);
            }
        });

    }

    private void startUploadSportData() {

        if (latLngs.size() <=2){
            toast("运动距离太短，忽略本次数据");
            latLngs.clear();
            mViewBinding.mapView.getMap().clear();
            return;
        }

        //本次运动时长
        long duration = (System.currentTimeMillis() - startTime) / 1000;

        //总距离
        double mileage = Double.parseDouble(mViewBinding.tvTotal.getText().toString());

        //点，轨迹
        String points = GsonUtils.toJson(latLngs);

        HashMap<String, String> body = new HashMap<>();
        body.put("duration", String.valueOf(duration));
        body.put("mileage", String.valueOf(mileage));
        body.put("points", points);

        HttpUtils.getInstance().post("sport/add", body, new HttpCallback(this) {
            @Override
            protected void onData(HttpUtils.ResultBean bean) {
                toast("保存运动数据成功");
                mViewBinding.tvDuration.setText("00:00:00");
                mViewBinding.tvTotal.setText("0.0");
                mViewBinding.tvSpeed.setText("0.0");

            }

            @Override
            protected void complete() {
                super.complete();

                latLngs.clear();
                mViewBinding.mapView.getMap().clear();
            }
        });

    }


    /**
     * 计算运动时长
     */
    private void exeDuration() {
        long duration = System.currentTimeMillis() - startTime;
        String durationStr = DateUtils.formatTime(duration / 1000);
        mViewBinding.tvDuration.setText(durationStr);
    }

    /**
     * 计算速度
     */
    private void exeSpeed() {
        if (latLngs.size() > 1) {
            LatLng end = latLngs.get(latLngs.size() - 1);
            LatLng second = latLngs.get(latLngs.size() - 2);
            double s = DistanceUtil.getDistance(end, second);
            //一小时运动的距离
            double total = (s * 60 * 60) / 1000.0;
            mViewBinding.tvSpeed.setText(String.format("%.1f", total));
        }
    }

    /**
     * 计算总里程
     */
    private void exeSum() {
        int total = 0;

        LatLng start;
        if (latLngs.size() > 1) {
            start = latLngs.get(0);
            for (int i = 1; i < latLngs.size(); i++) {

                LatLng latLng = latLngs.get(i);
                total += DistanceUtil.getDistance(start, latLng);
                start = latLng;
            }
        }

        double t = total / 1000.0;

        mViewBinding.tvTotal.setText(String.format("%.1f", t));
    }
}
