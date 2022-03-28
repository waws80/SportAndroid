package edu.wj.sport.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.wj.sport.android.SportApplication;

public class SportDataUtils implements LifecycleOwner {

    private SharedPreferences sportSp;

    private final LifecycleRegistry registry = new LifecycleRegistry(this);


    private final AtomicBoolean executing = new AtomicBoolean(false);

    private static final class Builder{
        private static final SportDataUtils UTILS = new SportDataUtils();
    }

    private SportDataUtils(){
        this.sportSp = SportApplication.getApplication()
                .getSharedPreferences("sport_cache_data", Context.MODE_PRIVATE);
        registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!executing.get()){
                    append(getData());
                }
            }
        }, 180, 180, TimeUnit.SECONDS);
    }

    public static SportDataUtils getInstance(){
        return Builder.UTILS;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return registry;
    }

    public void append(SportDataBean sportDataBean){
        if (sportDataBean == null){
            return;
        }
        if (this.executing.get()){
            addCache(sportDataBean);
            return;
        }
        this.executing.set(true);
        HashMap<String, String> body = new HashMap<>();
        body.put("sportType", sportDataBean.sportType.name());
        body.put("distance", String.valueOf(sportDataBean.distance));
        body.put("power", String.valueOf(sportDataBean.power));
        body.put("timeLen", String.valueOf(sportDataBean.timeLen));
        Log.d("timeLen", sportDataBean.timeLen + "");
        body.put("userId", String.valueOf(UserDefault.getInstance().getUserInfo().getId()));
        HttpUtils.getInstance().post("sport/add", body, new HttpUtils.DataCallback(this) {
            @Override
            protected void onError(HttpUtils.ResultBean bean) {
                addCache(sportDataBean);
                SportDataUtils.this.executing.set(false);
            }

            @Override
            protected void onData(HttpUtils.ResultBean bean) {
                if (sportSp.contains(sportDataBean.id)){
                    sportSp.edit().remove(sportDataBean.id).apply();
                }
                SportDataUtils.this.executing.set(false);
            }
        });
    }

    private void addCache(SportDataBean bean) {
        sportSp.edit().putString(bean.id, GsonUtils.toJson(bean)).apply();
    }

    private SportDataBean getData(){
        Map<String, ?> all = sportSp.getAll();
        if (all.isEmpty()){
            return null;
        }
        Collection<?> values = all.values();
        Object next = values.iterator().next();
        if (next == null){
            return null;
        }
        return GsonUtils.toEntity(SportDataBean.class, next);
    }




    public static class SportDataBean{

        public SportType sportType;

        public double distance;

        public int power;

        public long timeLen;

        public String id = System.currentTimeMillis() + "";

        public SportDataBean(SportType sportType, double distance, int power, long timeLen) {
            this.sportType = sportType;
            this.distance = distance;
            this.power = power;
            this.timeLen = timeLen;
        }
    }

    public static enum SportType{

        WALKING,

        RUNNING,

        CYCLING
    }
}
