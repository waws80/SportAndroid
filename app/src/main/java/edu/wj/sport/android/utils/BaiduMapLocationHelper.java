package edu.wj.sport.android.utils;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.wj.sport.android.R;
import edu.wj.sport.android.SportApplication;

public final class BaiduMapLocationHelper  extends BDAbstractLocationListener implements LifecycleObserver {

    private static final String TAG = "BaiduMapLocationHelper";

    private WeakReference<LifecycleOwner> ownerWeakReference;

    private WeakReference<MapView> mapViewWeakReference;

    private WeakReference<BaiduMap> mapWeakReference;

    private WeakReference<LocationClient> locationClientWeakReference;

    private OnLocation mOnLocation;

    private final AtomicBoolean firstLocation = new AtomicBoolean(true);

    private final AtomicBoolean flashMapLocation = new AtomicBoolean(false);


    public void bind(LifecycleOwner owner, MapView mapView){
        this.bind(owner, mapView, null);
    }

    public void bind(LifecycleOwner owner, MapView mapView, OnLocation onLocation){
        this.ownerWeakReference = new WeakReference<>(owner);
        this.mapViewWeakReference = new WeakReference<>(mapView);
        this.mapWeakReference = new WeakReference<>(mapView.getMap());
        this.mOnLocation = onLocation;
        owner.getLifecycle().addObserver(this);
        this.locationClientWeakReference = new WeakReference<>(new LocationClient(SportApplication.getApplication()));
    }

    /**
     * 是否根据定位信息刷新当前地图显示位置
     * @param flash
     */
    public void setFlashMapLocation(boolean flash){
        flashMapLocation.set(flash);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate(){
        Log.d(TAG, "onCreate");
        if (this.mapWeakReference == null){
            return;
        }
        if (this.locationClientWeakReference == null){
            return;
        }
        BaiduMap map = this.mapWeakReference.get();
        if (map != null){
            //打开室内图，默认为关闭状态
            map.setIndoorEnable(true);
            map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            map.setMyLocationEnabled(true);
            MyLocationConfiguration configuration =
                    new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,
                            true,
                            BitmapDescriptorFactory.fromResource(R.drawable.map_icon_start),
                            0xAAFFFF88,
                            0xAA00FF00);
            map.setMyLocationConfiguration(configuration);
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume(){
        Log.d(TAG, "onResume");
        if (this.mapViewWeakReference == null){
            return;
        }
        MapView mapView = this.mapViewWeakReference.get();
        if (mapView == null){
            return;
        }
        mapView.onResume();

        LocationClient client = this.locationClientWeakReference.get();
        if (client == null){
            return;
        }
        if (client.isStarted()){
            return;
        }
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        option.setIsNeedAddress(true);
        option.setNeedNewVersionRgc(true);
        option.setIgnoreKillProcess(true);
        //设置locationClientOption
        client.setLocOption(option);
        //注册LocationListener监听器
        client.registerLocationListener(this);
        //开启地图定位图层
        client.start();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause(){
        Log.d(TAG, "onPause");
        if (this.mapViewWeakReference == null){
            return;
        }
        MapView mapView = this.mapViewWeakReference.get();
        if (mapView == null){
            return;
        }
        mapView.onPause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner){
        if (this.locationClientWeakReference != null){
            LocationClient client = this.locationClientWeakReference.get();
            if (client != null){
                client.stop();
            }
            client = null;
            this.locationClientWeakReference.clear();
        }
        if (this.mapWeakReference != null){
            BaiduMap map = this.mapWeakReference.get();
            if (map != null){
                map.setMyLocationEnabled(false);
            }
            map = null;
            this.mapWeakReference.clear();
        }
        if (this.mapViewWeakReference != null){
            MapView mapView = this.mapViewWeakReference.get();
            if (mapView != null){
                mapView.onDestroy();
            }
            mapView = null;
            this.mapViewWeakReference.clear();
        }
        owner.getLifecycle().removeObserver(this);

        if (this.ownerWeakReference != null){
            this.ownerWeakReference.clear();
        }
        this.mOnLocation = null;
        this.firstLocation.set(true);
        Log.d(TAG, "onDestroy");
    }


    @Override
    public void onReceiveLocation(BDLocation location) {
        Log.d(TAG, "onReceiveLocation: ");
        if (this.ownerWeakReference == null){
            return;
        }
        if (this.mapViewWeakReference == null){
            return;
        }
        if (this.mapWeakReference == null){
            return;
        }
        LifecycleOwner owner = this.ownerWeakReference.get();
        if (owner == null){
            return;
        }

        MapView mapView = this.mapViewWeakReference.get();

        if (location == null || mapView == null){
            return;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.getDirection()).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();

        if (location.getLocType() == 61 || location.getLocType() == 66 || location.getLocType() == 161){
            if (location.getAddrStr() == null){
                return;
            }
            if (this.mOnLocation != null){
                if (firstLocation.get()){
                    this.mOnLocation.onFirstLocation(location);
                    this.markStart(location.getLatitude(), location.getLongitude(), R.drawable.map_icon_start);

                }
                this.mOnLocation.onLocation(location);
            }
        }

        if (owner.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED && (this.flashMapLocation.get() || this.firstLocation.get())){
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(16.0f);
            BaiduMap baiduMap = this.mapWeakReference.get();
            if (baiduMap == null){
                return;
            }
            baiduMap.setMyLocationData(locData);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            firstLocation.set(false);
        }
    }


    /**
     * 标记点
     * @param lat
     * @param lon
     * @param res
     */
    public void markStart(double lat, double lon, int res){

        //定义Maker坐标点
        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(res);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .scaleX(0.5f)
                .scaleY(0.5f)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        if (this.mapWeakReference == null){
            return;
        }
        BaiduMap map = this.mapWeakReference.get();
        if (map == null){
            return;
        }
        map.addOverlay(option);
    }



    public interface OnLocation{

        void onFirstLocation(BDLocation location);

        void onLocation(BDLocation location);
    }
}
