package com.github.naz013.clockviewlibrary;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.naz013.clockview.ClockView;

public class MainActivity extends AppCompatActivity {

    private ClockView clockView;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            clockView.setTime(System.currentTimeMillis());
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clockView = findViewById(R.id.clockView);
        mHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}
