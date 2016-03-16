package com.cabbage.mapinfoplayground;

import android.content.Context;
import android.databinding.BaseObservable;

public class BaseViewModel<T> extends BaseObservable {

    protected final Context mContext;
    protected T mModel;

    public BaseViewModel(Context context) {
        this(context, null);
    }

    public BaseViewModel(Context context, T model) {
        this.mContext = context;
        this.mModel = model;
    }

    public void setModel(T model) {
        this.mModel = model;
    }
}
