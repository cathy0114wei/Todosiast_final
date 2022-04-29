package com.example.mobile_final;

import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    public CheckBox checkBox;
    public TextView textView;
    public TextView finish;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        textView = mView.findViewById(R.id.taskTv);
        finish = mView.findViewById(R.id.finish);
        checkBox = itemView.findViewById(R.id.checkBox);
    }

    public void setTask(String task) {
        textView.setText(task);
    }

    public void setDesc(String desc) {
        TextView descTextView = mView.findViewById(R.id.descriptionTv);
        descTextView.setText(desc);
    }

    public void setDate(String date) {
        TextView dateTextView = mView.findViewById(R.id.dateTv);
        dateTextView.setText(date);
    }

    public void setFinish(boolean status) {
        if (status) {
            finish.setVisibility(View.VISIBLE);
            checkBox.setChecked(true);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            checkBox.setChecked(false);
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            finish.setVisibility(View.GONE);
        }
    }
}