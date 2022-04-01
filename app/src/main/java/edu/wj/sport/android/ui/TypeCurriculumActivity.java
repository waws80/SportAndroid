package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import edu.wj.sport.android.adapter.CurriculumAdapter;
import edu.wj.sport.android.bean.CurriculumBean;
import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivityTypeCurriculumBinding;
import edu.wj.sport.android.utils.HttpUtils;

public class TypeCurriculumActivity extends BaseActivity<ActivityTypeCurriculumBinding> {


    private final CurriculumAdapter adapter = new CurriculumAdapter();



    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {
        String type = getIntent().getStringExtra("type");

        mViewBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewBinding.rv.setLayoutManager(new GridLayoutManager(this, 1));
        mViewBinding.rv.setAdapter(adapter);

        getTypeDataList(type);



    }

    /**
     * 获取对应类型的课程数据
     * @param type
     */
    private void getTypeDataList(String type) {

        String path = "curriculum/find";
        if (type != null && !type.isEmpty()){
            path = path + "?type="+ type;
        }

        HttpUtils.getInstance().get(path, new HttpCallback(this) {
            @Override
            protected void onData(HttpUtils.ResultBean bean) {
                adapter.setData(bean.getList(CurriculumBean.class));
            }
        });

    }
}