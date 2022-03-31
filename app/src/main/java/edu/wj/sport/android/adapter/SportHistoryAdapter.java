package edu.wj.sport.android.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.wj.sport.android.bean.SportBean;
import edu.wj.sport.android.databinding.ItemSportDataBinding;
import edu.wj.sport.android.ui.SportDetailActivity;
import edu.wj.sport.android.utils.DateUtils;
import edu.wj.sport.android.utils.GsonUtils;

public class SportHistoryAdapter extends RecyclerView.Adapter<SportHistoryAdapter.SportHistoryHolder> {

    private List<SportBean> data = new ArrayList<>();

    @NonNull
    @Override
    public SportHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSportDataBinding itemSportDataBinding = ItemSportDataBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new SportHistoryHolder(itemSportDataBinding.getRoot(), itemSportDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SportHistoryHolder holder, int position) {

        SportBean bean = this.data.get(holder.getAdapterPosition());
        holder.dataBinding.tvTime.setText(DateUtils.formatMill(bean.getCreateTime()));
        holder.dataBinding.tvMileage.setText(String.format("总距离 %.1f 千米", bean.getMileage()));
        holder.dataBinding.tvDuration.setText(String.format("总时长 %s", DateUtils.formatTime(bean.getDuration())));
        holder.dataBinding.tvSpeed.setText(String.format("平均速度 %.1f 千米", (bean.getMileage() / (60 * 60))));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), SportDetailActivity.class);
                intent.putExtra("item", GsonUtils.toJson(bean));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public void setData(List<SportBean> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public static class SportHistoryHolder extends RecyclerView.ViewHolder{

        private final ItemSportDataBinding dataBinding;

        public SportHistoryHolder(@NonNull View itemView, ItemSportDataBinding dataBinding) {
            super(itemView);
            this.dataBinding = dataBinding;
        }

    }
}
