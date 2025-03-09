package com.example.decibelz;

import androidx.lifecycle.MutableLiveData;

public class LiveData {

    private static final LiveData instance = new LiveData();
    private final MutableLiveData<Integer> data = new MutableLiveData<>();
    private final MutableLiveData<Integer> highest = new MutableLiveData<>();
    private final MutableLiveData<Integer> lowest = new MutableLiveData<>();

    public static LiveData get() {
        return instance;
    }

    public MutableLiveData<Integer> getData() {
        return data;
    }

    public MutableLiveData<Integer> getHighest() {
        return highest;
    }

    public MutableLiveData<Integer> getLowest() {
        return lowest;
    }

}
