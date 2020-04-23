package com.boala.mathtrainer;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResAdapter extends RecyclerView.Adapter<ResAdapter.ResHolder> {
    private Context context;
    private ArrayList<Result> content;

    ResAdapter(Context context, ArrayList<Result> content){
        this.context = context;
        this.content = content;
    }

    @NonNull
    @Override
    public ResAdapter.ResHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_res,parent,false);
        return new ResHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResAdapter.ResHolder holder, int position) {
        Result data = content.get(position);
        holder.setData(data);

    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    class ResHolder extends RecyclerView.ViewHolder{
        private TextView tv,tvSol;

        ResHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tvRes);
            tvSol = itemView.findViewById(R.id.tvSol);
        }

        void setData(Result data) {
            tv.setText(data.getText());
            if (data.isCorrect()) {
                tv.setTextColor(Color.parseColor("#66bb6a"));
            }else{
                tv.setTextColor(Color.parseColor("#ef5350"));
            }
            tvSol.setText(data.getTextCorrect());
        }
    }
}
