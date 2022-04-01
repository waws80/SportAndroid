package edu.wj.sport.android.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.wj.sport.android.adapter.CurriculumAdapter;
import edu.wj.sport.android.bean.CurriculumBean;
import edu.wj.sport.android.common.BaseActivity;
import edu.wj.sport.android.databinding.ActivitySearchBinding;
import edu.wj.sport.android.utils.HttpUtils;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> {

    private final CurriculumAdapter adapter = new CurriculumAdapter();

    @Override
    protected void onLoad(@Nullable Bundle savedInstanceState) {


        mViewBinding.rv.setLayoutManager(new GridLayoutManager(this, 1));
        mViewBinding.rv.setAdapter(adapter);


        mViewBinding.edtSearch.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewBinding.edtSearch.requestFocus();
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(mViewBinding.edtSearch, 0);
            }
        }, 1000);

        mViewBinding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    getTypeDataList(mViewBinding.edtSearch.getText().toString());
                }

                /*隐藏软键盘*/
                InputMethodManager imm = (InputMethodManager) v
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(
                            v.getApplicationWindowToken(), 0);
                }

                return true;
            }
        });

    }

    /**
     * 获取课程数据
     * @param search 搜索内容
     */
    private void getTypeDataList(String search) {

        String path = "curriculum/find";

        HttpUtils.getInstance().get(path, new HttpCallback(this) {
            @Override
            protected void onData(HttpUtils.ResultBean bean) {
                List<CurriculumBean> list = new ArrayList<>();

                for (CurriculumBean curriculumBean : bean.getList(CurriculumBean.class)) {
                    if (curriculumBean.getCname().contains(search)){
                        list.add(curriculumBean);
                    }
                }

                adapter.setData(list);
            }
        });

    }
}