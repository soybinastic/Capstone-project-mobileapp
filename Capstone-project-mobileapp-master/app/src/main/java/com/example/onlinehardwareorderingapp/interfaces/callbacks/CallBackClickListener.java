package com.example.onlinehardwareorderingapp.interfaces.callbacks;

import com.example.onlinehardwareorderingapp.interfaces.BaseModel;

public interface CallBackClickListener extends Invoker<BaseModel, String>{
    void onNavigate();
}
