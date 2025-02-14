package com.example.employee_management_app;

public interface ApiCallback<T> {
    void onSuccess(T result);
    void onError(Exception e);
}