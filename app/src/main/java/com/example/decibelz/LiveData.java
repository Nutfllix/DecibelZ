package com.example.decibelz;

import androidx.lifecycle.MutableLiveData;

public class LiveData {

    private static final LiveData instance = new LiveData();
    private final MutableLiveData<String> data = new MutableLiveData<>();

    public static LiveData get() {
        return instance;
    }

    public MutableLiveData<String> getData() {
        return data;
    }

}
