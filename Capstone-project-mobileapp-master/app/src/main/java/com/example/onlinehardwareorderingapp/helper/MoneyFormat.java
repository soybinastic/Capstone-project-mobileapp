package com.example.onlinehardwareorderingapp.helper;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class MoneyFormat {
    public static String getFormat(double val){
//        Locale philippines = new Locale("es", "ES");
//        Currency peso = Currency.getInstance(Local);
//        NumberFormat pesoFormat = NumberFormat.getCurrencyInstance();

        return "PHP" + String.format("%,.2f", val);
    }
}
