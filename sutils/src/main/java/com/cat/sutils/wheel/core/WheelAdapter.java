package com.cat.sutils.wheel.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cat.sutils.R;

import java.util.ArrayList;
import java.util.List;

class WheelAdapter extends RecyclerView.Adapter<WheelAdapter.WheelViewHolder> {

    private List<WheelDataItem> mData;

    private Context context;

    private View.OnClickListener onClickListener;

    public WheelAdapter(Context context,View.OnClickListener onClickListener){
        this.context=context;
        this.onClickListener=onClickListener;
        this.mData=new ArrayList<>();
    }

    public <T extends WheelDataItem> void  update(List<T> data){
        this.mData.clear();
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WheelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WheelViewHolder(LayoutInflater.from(context).inflate(R.layout.wheel_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WheelViewHolder wheelViewHolder, int i) {
        wheelViewHolder.tvWheel.setText(getItem(i).getName());
        wheelViewHolder.tvWheel.setOnClickListener(onClickListener);
    }


    public WheelDataItem getItem(int position){
        return mData.get(position);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    class WheelViewHolder extends RecyclerView.ViewHolder{

        TextView tvWheel;

        public WheelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWheel= (TextView) itemView;
        }
    }
}
