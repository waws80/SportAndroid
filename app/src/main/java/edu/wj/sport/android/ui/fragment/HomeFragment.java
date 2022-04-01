package edu.wj.sport.android.ui.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import edu.wj.sport.android.adapter.CurriculumAdapter;
import edu.wj.sport.android.bean.CurriculumBean;
import edu.wj.sport.android.common.BaseFragment;
import edu.wj.sport.android.databinding.FragmentHomeBinding;
import edu.wj.sport.android.ui.SearchActivity;
import edu.wj.sport.android.ui.TypeCurriculumActivity;
import edu.wj.sport.android.utils.HttpUtils;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    CurriculumAdapter adapter = new CurriculumAdapter();

    @Override
    protected void onLoad() {

        //获取推荐课程

        getRecommend();

        mViewBinding.rv.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        mViewBinding.rv.setAdapter(adapter);


        mViewBinding.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(SearchActivity.class);
            }
        });

        Intent intent = new Intent(requireContext(), TypeCurriculumActivity.class);

        mViewBinding.tvRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type","跑步");
                startActivity(intent);
            }
        });

        mViewBinding.tvBycycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type","骑行");
                startActivity(intent);
            }
        });

        mViewBinding.tvRope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type","跳绳");
                startActivity(intent);
            }
        });

        mViewBinding.tvAerobics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type","健身操");
                startActivity(intent);
            }
        });

        mViewBinding.tvYoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type","瑜伽");
                startActivity(intent);
            }
        });

        mViewBinding.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type","");
                startActivity(intent);
            }
        });


    }

    private void getRecommend() {
        HttpUtils.getInstance().get("curriculum/recommend", new HttpCallback(this) {
            @Override
            protected void onData(HttpUtils.ResultBean bean) {
                adapter.setData(bean.getList(CurriculumBean.class));
            }
        });
    }
}
