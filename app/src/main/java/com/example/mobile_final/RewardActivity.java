package com.example.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class RewardActivity extends AppCompatActivity {
    ImageView image1;
    ImageView image5;
    ImageView image10;
    Button button;
    LinearLayout linearLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        Intent intent = getIntent();
        int finishedCount = intent.getIntExtra("FinishedCount", 0);
        Log.d("RewardActivity", finishedCount+"");
        TextView count = findViewById(R.id.finish_count);
        count.setText("Finished " + finishedCount + " tasks");

        image1 = findViewById(R.id.image1);
        image5 = findViewById(R.id.image5);
        image10 = findViewById(R.id.image10);

        linearLayout = findViewById(R.id.linearLayout);

        button = findViewById(R.id.reward_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finishedCount <= 0) {
                    Snackbar snackbar = Snackbar.make(linearLayout, "Keep working on your first task", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if(finishedCount > 0 && finishedCount < 5) {
                    image1.setVisibility(View.VISIBLE);
                    Snackbar snackbar = Snackbar.make(linearLayout, "Congratulation, keep working to earn the next reward", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if(finishedCount >= 5 && finishedCount < 10) {
                    image5.setVisibility(View.VISIBLE);
                    image1.setVisibility(View.VISIBLE);
                    Snackbar snackbar = Snackbar.make(linearLayout, "Congratulation, keep working to earn the next reward", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    image5.setVisibility(View.VISIBLE);
                    image1.setVisibility(View.VISIBLE);
                    image10.setVisibility(View.VISIBLE);
                    Snackbar snackbar = Snackbar.make(linearLayout, "Congratulation, you've earned all the rewards", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
}