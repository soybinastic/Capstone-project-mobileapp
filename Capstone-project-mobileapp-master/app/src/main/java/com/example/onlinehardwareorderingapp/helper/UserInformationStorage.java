package com.example.onlinehardwareorderingapp.helper;

import android.content.SharedPreferences;

public class UserInformationStorage {
    private static UserInformationStorage userInformationStorage = null;
    private SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "USER_INFO";
    private UserInformationStorage(){}
    public static UserInformationStorage getInstance(){
        if (userInformationStorage == null){
            userInformationStorage = new UserInformationStorage();
        }
        return userInformationStorage;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
    public SharedPreferences.Editor getEditor(){
        return sharedPreferences.edit();
    }

}
