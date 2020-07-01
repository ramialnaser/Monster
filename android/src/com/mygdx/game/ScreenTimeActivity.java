package com.mygdx.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ScreenTimeActivity extends AppCompatActivity {

    private TextView todayText;
    private TextView weekText;
    private TextView monthText;
    private TextView yearText;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_time);

        todayText = findViewById(R.id.screentime_todayTime);
        weekText = findViewById(R.id.screentime_time7days);
        monthText = findViewById(R.id.screentime_time30days);
        yearText = findViewById(R.id.screentime_time12month);
    }
}
