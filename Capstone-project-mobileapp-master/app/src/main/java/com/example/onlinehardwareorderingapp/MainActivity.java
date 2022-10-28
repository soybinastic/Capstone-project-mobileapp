package com.example.onlinehardwareorderingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.onlinehardwareorderingapp.activities.GetStartedActivity;
import com.example.onlinehardwareorderingapp.activities.IntroActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String ENTRY_POINT = "first_time_user";
    private static final String GOTO_GET_STARTED = "is_user_first_time";
    private static final String VALUE = "YES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    void init(){
        sharedPreferences = getSharedPreferences(ENTRY_POINT, MODE_PRIVATE);
//        editor = sharedPreferences.edit();
        String value = sharedPreferences.getString(GOTO_GET_STARTED, "");
        //Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        Intent intent;
        if (!value.equals(VALUE)){
//            editor.putString(GOTO_GET_STARTED, VALUE);
//            editor.commit();
            intent = new Intent(this, GetStartedActivity.class);
        }else {
            intent = new Intent(this, IntroActivity.class);
        }
        finish();
        startActivity(intent);
    }
}