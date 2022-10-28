package com.example.onlinehardwareorderingapp.helper;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.onlinehardwareorderingapp.R;

public class ButtonWithProgressBar {
    private String s1;
    private String s2;
    private ConstraintLayout btnConstraint;
    private ProgressBar progressBar;
    private TextView textView;
    private Context context;
    public ButtonWithProgressBar(View view, Context context, String s1, String s2){
        btnConstraint = view.findViewById(R.id.btn_progress_bar);
        progressBar = view.findViewById(R.id.progress_bar_btn);
        textView = view.findViewById(R.id.progress_btn_text);
        textView.setText(s1);
        this.s1 = s1;
        this.s2 = s2;
    }


    public void activateProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText(s2);
    }
    public void finish(){
        progressBar.setVisibility(View.GONE);
        textView.setText(s1);
    }
}
