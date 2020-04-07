package com.ousy.aopdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ousy.aopmodule.annotations.LimitClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @LimitClick(value = 2000, viewId = R.id.tv_test)
    private TextView tvTest;
    @LimitClick(value = 2000, viewId = R.id.btn_test)
    private Button btnTest;
    private Button btnTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTest = findViewById(R.id.tv_test);
        btnTest = findViewById(R.id.btn_test);
        btnTest2 = findViewById(R.id.btn_test2);
        tvTest.setOnClickListener(this);
        btnTest.setOnClickListener(this);
        btnTest2.setOnClickListener(this);
        onTest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int i = 0;
        i++;
    }

    @Override
    protected void onStart() {
        super.onStart();
        int i = 0;
        i++;
    }

    public void onTest() {
        Log.e("ousyxx", "aaaaa");
    }

    @Override
    public void onClick(View view) {
        Log.e("ousyxx","click");
    }
}
