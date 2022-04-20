package com.example.mobile_final;

import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FinishViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    public CheckBox checkBox;
    public TextView textView;
    public TextView finish;

    public FinishViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        textView = mView.findViewById(R.id.TaskTitle);
        finish = mView.findViewById(R.id.finish2);
        checkBox = itemView.findViewById(R.id.checkBox2);
    }

    public void setTask(String task) {
        textView.setText(task);
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
