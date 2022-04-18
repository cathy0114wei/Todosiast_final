package com.example.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class RewardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        Intent intent = getIntent();
        int finishedCount = intent.getIntExtra("FinishedCount", 0);
        Log.d("RewardActivity", finishedCount+"");
        TextView count = findViewById(R.id.finish_count);
        count.setText("Finished " + finishedCount + " tasks");
    }
}