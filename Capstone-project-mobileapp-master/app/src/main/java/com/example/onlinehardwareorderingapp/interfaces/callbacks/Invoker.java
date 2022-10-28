package com.example.onlinehardwareorderingapp.interfaces.callbacks;

public interface Invoker<T, TArgs> {
    void onClick(T data, TArgs args);
}
