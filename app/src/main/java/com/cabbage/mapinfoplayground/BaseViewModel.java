package com.cabbage.mapinfoplayground;

import android.content.Context;
import android.databinding.BaseObservable;

public class BaseViewModel extends BaseObservable {

    protected final Context mContext;

    public BaseViewModel(Context context) {
        mContext = context;
    }
}
