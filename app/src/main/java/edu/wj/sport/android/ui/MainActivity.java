package edu.wj.sport.android.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.wj.sport.android.R;
import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivityMainBinding;
import edu.wj.sport.android.ui.fragment.HomeFragment;
import edu.wj.sport.android.ui.fragment.MineFragment;
import edu.wj.sport.android.ui.fragment.SportFragment;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private final List<Fragment> fragmentList = new ArrayList<>();

    private final ExecutorService animationThread = Executors.newSingleThreadExecutor();

    private final FragmentStateAdapter adapter = new FragmentStateAdapter(this) {


        @Override
        public int getItemCount() {
            return fragmentList.size();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }
    };


    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {
        fragmentList.add(new HomeFragment());
        fragmentList.add(new SportFragment());
        fragmentList.add(new MineFragment());
        mViewBinding.viewPager.setUserInputEnabled(false);
        mViewBinding.viewPager.setAdapter(adapter);
        mViewBinding.bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_main){
                    mViewBinding.viewPager.setCurrentItem(0, false);
                }else if (item.getItemId() == R.id.menu_sport){
                    mViewBinding.viewPager.setCurrentItem(1, false);
                }else {
                    mViewBinding.viewPager.setCurrentItem(2, false);
                }
                return true;
            }
        });

        requestPermission();

    }

    public void changeTabBar(boolean running){
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56f, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams layoutParams = mViewBinding.bottomView.getLayoutParams();
        if (running){
            layoutParams.height = 0;
        }else {
            layoutParams.height = height;
        }
        mViewBinding.bottomView.setLayoutParams(layoutParams);
    }


    private void requestPermission(){
        String [] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BODY_SENSORS};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BODY_SENSORS, Manifest.permission.ACTIVITY_RECOGNITION};
        }
        requestPermissions(permissions, 100);
    }
}