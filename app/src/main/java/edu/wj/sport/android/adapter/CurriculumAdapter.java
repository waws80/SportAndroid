package edu.wj.sport.android.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import edu.wj.sport.android.bean.CurriculumBean;
import edu.wj.sport.android.bean.SportBean;
import edu.wj.sport.android.databinding.ItemCurriculumDataBinding;
import edu.wj.sport.android.databinding.ItemSportDataBinding;
import edu.wj.sport.android.ui.CurriculumDetailActivity;
import edu.wj.sport.android.ui.SportDetailActivity;
import edu.wj.sport.android.utils.DateUtils;
import edu.wj.sport.android.utils.GsonUtils;
import edu.wj.sport.android.utils.HttpUtils;

public class CurriculumAdapter extends RecyclerView.Adapter<CurriculumAdapter.SportHistoryHolder> {

    private List<CurriculumBean> data = new ArrayList<>();

    @NonNull
    @Override
    public SportHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCurriculumDataBinding itemSportDataBinding = ItemCurriculumDataBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new SportHistoryHolder(itemSportDataBinding.getRoot(), itemSportDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SportHistoryHolder holder, int position) {

        CurriculumBean bean = this.data.get(holder.getAdapterPosition());

        holder.dataBinding.tvName.setText("视频名称：" + bean.getCname());
        holder.dataBinding.tvTypeDuration.setText("类型：" + bean.getType() + "      视频时长："+ bean.getDuration() + "秒" );
        holder.dataBinding.tvTip.setText("标签：" + bean.getTip());

        Glide.with(holder.dataBinding.ivThumb).load(HttpUtils.RES_URL + "img/" + bean.getThumb()).centerCrop().into(holder.dataBinding.ivThumb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), CurriculumDetailActivity.class);
                intent.putExtra("item", GsonUtils.toJson(bean));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public void setData(List<CurriculumBean> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public static class SportHistoryHolder extends RecyclerView.ViewHolder{

        private final ItemCurriculumDataBinding dataBinding;

        public SportHistoryHolder(@NonNull View itemView, ItemCurriculumDataBinding dataBinding) {
            super(itemView);
            this.dataBinding = dataBinding;
        }

    }
}
