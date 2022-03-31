package edu.wj.sport.android.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import java.util.HashMap;

import edu.wj.sport.android.adapter.SportHistoryAdapter;
import edu.wj.sport.android.bean.SportBean;
import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivitySportHistoryBinding;
import edu.wj.sport.android.utils.HttpUtils;
import edu.wj.sport.android.utils.UserDefault;

public class SportHistoryActivity extends BaseActivity<ActivitySportHistoryBinding> {

    private final SportHistoryAdapter sportHistoryAdapter = new SportHistoryAdapter();

    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {

        mViewBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewBinding.rv.setLayoutManager(new GridLayoutManager(this, 1));
        mViewBinding.rv.setAdapter(sportHistoryAdapter);

        mViewBinding.ll.post(new Runnable() {
            @Override
            public void run() {
                mViewBinding.rv.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        if (parent.getChildAdapterPosition(view) == 0){
                            outRect.top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    152, getResources().getDisplayMetrics());
                        }
                    }
                });

            }
        });

        loadOverride();

        loadHistory();
    }

    /**
     * 读取总览数据
     */
    private void loadOverride() {
        HttpUtils.getInstance().get("sport/overview?userId=" + UserDefault.getInstance().getUserId(),
                new HttpCallback(this) {
            @Override
            protected void onData(HttpUtils.ResultBean bean) {
                HashMap<String, String> data = bean.getEntity(HashMap.class);
                mViewBinding.tvMileage.setText(data.get("sum"));
                mViewBinding.tvDuration.setText(data.get("time"));
            }
        });
    }


    private void loadHistory(){
        HttpUtils.getInstance().get("sport/search?userId=" + UserDefault.getInstance().getUserId(),
                new HttpCallback(this) {
                    @Override
                    protected void onData(HttpUtils.ResultBean bean) {
                        sportHistoryAdapter.setData(bean.getList(SportBean.class));
                    }
                });
    }
}