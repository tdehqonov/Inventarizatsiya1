package com.example.inventarizatsiya1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnViewInformation, btnScanBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        btnViewInformation = (Button) findViewById(R.id.btnViewInformation);
        btnScanBarcode = (Button) findViewById(R.id.btnScanBarcode);
        btnViewInformation.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnViewInformation:
                //startActivity(new Intent(MainActivity.this, SQLView.class));
                break;
            case R.id.btnScanBarcode:
                startActivity(new Intent(MainActivity.this, activity_scan_barcode.class));
                break;
        }


    }
}