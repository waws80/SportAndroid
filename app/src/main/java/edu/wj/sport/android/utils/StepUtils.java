package edu.wj.sport.android.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import edu.wj.sport.android.SportApplication;

public class StepUtils implements SensorEventListener {

    private final SensorManager sensorManager;
    private Sensor sensorStepCounter;

    private final AtomicBoolean supportStep = new AtomicBoolean(false);

    private StepCountCallback callback;

    private RunningCallback runningCallback;


    private final AtomicInteger initCounter = new AtomicInteger(-1);


    private final ArrayList<Float> runningValues = new ArrayList<>();

    private ScheduledExecutorService runningService;


    private StepUtils(){
        sensorManager = (SensorManager) SportApplication.getApplication().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null){
            sensorStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (sensorStepCounter != null){
                supportStep.set(true);
            }
        }
    }

    public static StepUtils getInstance(){
        return new StepUtils();
    }


    public boolean supportStepCount(){
        return supportStep.get();
    }

    public void registerStepCounter(LifecycleOwner owner, StepCountCallback callback){
        if (callback == null){
            return;
        }
        if (supportStepCount()){
            sensorManager.registerListener(this, sensorStepCounter, SensorManager.SENSOR_DELAY_GAME);
            this.callback = callback;
            owner.getLifecycle().addObserver(new LifecycleEventObserver(){

                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY){
                        source.getLifecycle().removeObserver(this);
                        StepUtils.this.callback = null;
                        sensorManager.unregisterListener(StepUtils.this, sensorStepCounter);
                    }
                }
            });
        }else {
            callback.unSupport();
        }
    }


    public void registerRunning(LifecycleOwner owner, RunningCallback callback){
        if (callback == null){
            return;
        }
        if (sensorManager == null){
            return;
        }
        this.runningCallback = callback;
        Sensor defaultSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (defaultSensor == null){
            callback.unSupport();
            return;
        }
        sensorManager.registerListener(this, defaultSensor, SensorManager.SENSOR_DELAY_NORMAL);
        owner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY){
                    source.getLifecycle().removeObserver(this);
                    StepUtils.this.runningCallback = null;
                    if (StepUtils.this.runningService != null){
                        StepUtils.this.runningService.shutdownNow();
                        StepUtils.this.runningService = null;
                    }
                    sensorManager.unregisterListener(StepUtils.this, defaultSensor);
                }
            }
        });
        if (runningService == null){
            runningService = Executors.newSingleThreadScheduledExecutor();
            runningService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {

                    synchronized (runningValues){
                        if (runningValues.isEmpty()){
                            return;
                        }
                        Float max = Collections.max(runningValues);
                        Float min = Collections.min(runningValues);

                        runningValues.clear();

                        if (max - min >  6 || (min < 0 && max - min > 2)){
                            ThreadUtils.runMain(new Runnable() {
                                @Override
                                public void run() {
                                    if (StepUtils.this.runningCallback != null){
                                        StepUtils.this.runningCallback.onNext(true);
                                    }
                                }
                            });
                        }else {
                            ThreadUtils.runMain(new Runnable() {
                                @Override
                                public void run() {
                                    if (StepUtils.this.runningCallback != null){
                                        StepUtils.this.runningCallback.onNext(false);
                                    }
                                }
                            });
                        }
                    }

                }
            }, 0, 3, TimeUnit.SECONDS);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            int count = (int) event.values[0];
            if (initCounter.get() == -1){
                initCounter.set(count);
            }
            if (this.callback != null){
                ThreadUtils.runMain(() -> {
                    int stepCount = count - initCounter.get();
                    StepUtils.this.callback.onNext(stepCount);
                    Log.d("StepUtils", "当前步数:" + stepCount);
                });
            }
        }else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float g = event.values[2];
            synchronized (runningValues){
                runningValues.add(g);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    public interface StepCountCallback{


        @MainThread
        void onNext(int count);


        void unSupport();
    }



    public interface RunningCallback{

        @MainThread
        void onNext(boolean running);

        void unSupport();
    }


}
