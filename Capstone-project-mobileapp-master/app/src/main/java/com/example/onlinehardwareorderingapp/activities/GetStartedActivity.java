package com.example.onlinehardwareorderingapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.onlinehardwareorderingapp.MainActivity;
import com.example.onlinehardwareorderingapp.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class GetStartedActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ConstraintLayout constraintGetStartedBtn;
    TextView termAndCondition;
    CheckBox agreeCb;
    //SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String ENTRY_POINT = "first_time_user";
    private static final String GOTO_GET_STARTED = "is_user_first_time";
    private static final String VALUE = "YES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        constraintGetStartedBtn = (ConstraintLayout) findViewById(R.id.getStartedBtn);
        termAndCondition = findViewById(R.id.term_and_condition_txt);
        agreeCb = findViewById(R.id.agree_cb);

        termAndCondition.setMovementMethod(LinkMovementMethod.getInstance());
        termAndCondition.setLinkTextColor(Color.BLACK);
        constraintGetStartedBtn.setEnabled(false);
        constraintGetStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(GetStartedActivity.this, "You click it!", Toast.LENGTH_LONG).show();
                sharedPreferences = getSharedPreferences(ENTRY_POINT, MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString(GOTO_GET_STARTED, VALUE);
                editor.commit();
                //Toast.makeText(GetStartedActivity.this, sharedPreferences.getString(GOTO_GET_STARTED,""), Toast.LENGTH_SHORT).show();
                navigate();
            }

        });

        termAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetStartedActivity.this, TermAndConditionsActivity.class);
                startActivity(intent);
            }
        });
        agreeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                constraintGetStartedBtn.setEnabled(b);
                Toast.makeText(GetStartedActivity.this, b ? "True" : "False", Toast.LENGTH_SHORT).show();
            }
        });
        //sharedPreferences = getSharedPreferences(String.valueOf(R.string.shared_pref), Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putBoolean(String.valueOf(R.string.shared_pref), true);
    }

    private void navigate(){
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
        finish();
    }
}