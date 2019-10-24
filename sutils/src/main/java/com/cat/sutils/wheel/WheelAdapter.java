package com.cat.sutils.wheel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cat.sutils.R;

import java.util.ArrayList;
import java.util.List;

public class WheelAdapter<T> extends RecyclerView.Adapter<WheelAdapter.WheelViewHolder>  {

    private List<T> mData;

    private Context context;

    private View.OnClickListener onClickListener;

    public WheelAdapter(Context context,View.OnClickListener onClickListener){
        this.context=context;
        this.onClickListener=onClickListener;
        this.mData=new ArrayList<>();
    }

    public void  update(List<T> data){
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
    public void onBindViewHolder(@NonNull WheelAdapter.WheelViewHolder wheelViewHolder, int i) {
        wheelViewHolder.tvWheel.setText(getItem(i).toString());
        wheelViewHolder.tvWheel.setOnClickListener(onClickListener);
    }


    public T getItem(int position){
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
