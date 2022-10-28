package com.example.onlinehardwareorderingapp.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserRule {
    private static UserRule userRule = null;
    private UserRule(){}

    public static UserRule getInstance(){
        if (userRule == null){
            userRule = new UserRule();
        }
        return userRule;
    }
    public boolean isLoggedIn(Date tokenExpDate, String authToken){
        Date dateNow = new Date();

        if (tokenExpDate == null){
            return false;
        }
//        boolean isTokenNotExpire = (tokenExpDate.getYear() == dateNow.getYear() && tokenExpDate.getMonth() == dateNow.getMonth()
//            && isDateTimeValid(tokenExpDate, dateNow));
        return ((!authToken.isEmpty() || authToken != null) && dateNow.compareTo(tokenExpDate) < 0);
    }
    private boolean isDateTimeValid(Date exp, Date dateNow){
        if (exp.getDate() >= dateNow.getDate() && exp.getHours() > dateNow.getHours()){
            return true;
        }else {
            return false;
        }
    }

    public Date stringToDate(String stringDate){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            Date date = formatter.parse(stringDate);
            return date;
        }catch(Exception ex){
            return null;
        }
    }

}
