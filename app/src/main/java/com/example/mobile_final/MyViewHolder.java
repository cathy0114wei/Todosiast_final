package com.example.mobile_final;

import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    public CheckBox checkBox;
    public TextView taskTectView;
    public TextView finish;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        taskTectView = mView.findViewById(R.id.taskTv);
        finish = mView.findViewById(R.id.finish);
        checkBox = itemView.findViewById(R.id.checkBox);
    }

    public void setTask(String task) {
        taskTectView.setText(task);
    }

    public void setDesc(String desc) {
        TextView descTectView = mView.findViewById(R.id.descriptionTv);
        descTectView.setText(desc);
    }

    public void setDate(String date) {
        TextView dateTextView = mView.findViewById(R.id.dateTv);
        dateTextView.setText(date);
    }

    public void setFinish(boolean status) {
        if(status) {
            finish.setVisibility(View.VISIBLE);
            checkBox.setChecked(true);
            taskTectView.setPaintFlags(taskTectView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            finish.setVisibility(View.GONE);
        }
    }
}