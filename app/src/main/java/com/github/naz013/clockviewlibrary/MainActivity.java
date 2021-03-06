package com.github.naz013.clockviewlibrary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.naz013.clockview.ClockView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
    private ClockView clockView;
    private TextView textView;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            showTime(System.currentTimeMillis());
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        clockView = findViewById(R.id.clockView);
        mHandler.postDelayed(mRunnable, 1000);
    }

    private void showTime(long millis) {
        clockView.setTime(millis);
        textView.setText(timeFormat.format(new Date(millis)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_more) {
            showGithub();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showGithub() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://github.com/naz013/clock-view"));
        startActivity(intent);
    }
}
