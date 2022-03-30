package edu.wj.sport.android.ui.fragment;

import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.model.LatLng;

import edu.wj.sport.android.common.BaseFragment;
import edu.wj.sport.android.databinding.FragmentSportBinding;
import edu.wj.sport.android.ui.MainActivity;
import edu.wj.sport.android.utils.BaiduMapLocationHelper;

public class SportFragment extends BaseFragment<FragmentSportBinding> {

    private final BaiduMapLocationHelper helper = new BaiduMapLocationHelper();


    @Override
    protected void onLoad() {

        helper.bind(this, mViewBinding.mapView, new BaiduMapLocationHelper.OnLocation() {
            @Override
            public void onFirstLocation(BDLocation location) {

            }

            @Override
            public void onLocation(BDLocation location) {

            }
        });

        mViewBinding.ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击");
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
}
