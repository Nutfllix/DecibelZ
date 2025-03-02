package com.example.decibelz;

import androidx.lifecycle.MutableLiveData;

public class LiveData {

    private static final LiveData instance = new LiveData();
    private final MutableLiveData<Integer> data = new MutableLiveData<>();

    public static LiveData get() {
        return instance;
    }

    public MutableLiveData<Integer> getData() {
        return data;
    }

}
