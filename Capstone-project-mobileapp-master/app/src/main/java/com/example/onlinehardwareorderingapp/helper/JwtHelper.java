package com.example.onlinehardwareorderingapp.helper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.Date;

public class JwtHelper {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decoder(String token){
        Base64.Decoder decoder = Base64.getUrlDecoder();
        return new String(decoder.decode(token));
    }

    public static JSONObject getData(String decodedData) throws JSONException {
        return new JSONObject(decodedData);
    }

    public static class Payload{
        private String userId;
        private Date expirationDate;
        private String userName;

        public Payload(String userId, Date expirationDate, String userName){
            this.userId = userId;
            this.expirationDate = expirationDate;
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Date getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
