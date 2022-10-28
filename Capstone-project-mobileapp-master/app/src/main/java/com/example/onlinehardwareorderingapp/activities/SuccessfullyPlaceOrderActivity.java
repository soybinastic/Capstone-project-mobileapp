package com.example.onlinehardwareorderingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.onlinehardwareorderingapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SuccessfullyPlaceOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_place_order);
        Button ok = (Button) findViewById(R.id.success_ok_btn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessfullyPlaceOrderActivity.this, HistoryOrdersActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}