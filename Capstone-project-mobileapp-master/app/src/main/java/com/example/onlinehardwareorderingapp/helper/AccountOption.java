package com.example.onlinehardwareorderingapp.helper;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.interfaces.BaseModel;

import java.util.ArrayList;

public class AccountOption implements BaseModel {
    private String code;
    private String title;
    private int drawableIcon;
    public AccountOption(String code, String title){
        this.code = code;
        this.title = title;
    }
    public AccountOption(String code, String title, int drawableIcon){
        this.code = code;
        this.title = title;
        this.drawableIcon = drawableIcon;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDrawableIcon() {
        return drawableIcon;
    }

    public void setDrawableIcon(int drawableIcon) {
        this.drawableIcon = drawableIcon;
    }


    public static class Dataset{

        public ArrayList<AccountOption> getOptions(){
            ArrayList<AccountOption> options = new ArrayList<>();
            options.add(new AccountOption(Keys.PERSONAL_INFORMATION_CODE, "Personal Information"));
            options.add(new AccountOption(Keys.LOCATION_CODE, "Location"));
            options.add(new AccountOption(Keys.CUSTOMER_ORDER_CODE, "Order"));
            options.add(new AccountOption("logout", "Logout", R.drawable.logout));
            
            return options;
        }
    }
}
