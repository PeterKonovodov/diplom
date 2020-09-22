package com.konovodov.diplom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("tag", "MainActivity Started");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("tag", "MainActivity onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("tag", "MainActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("tag", "MainActivity onDestroy");
    }
}

