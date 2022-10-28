package com.example.onlinehardwareorderingapp.helper;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.example.onlinehardwareorderingapp.R;

public class LoadingAlertDialog {
    private Activity activity;
    private AlertDialog alertDialog;
    public LoadingAlertDialog(Activity activity){
        this.activity = activity;
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.loading_alert_dialog, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismiss(){
        alertDialog.dismiss();
    }
}
