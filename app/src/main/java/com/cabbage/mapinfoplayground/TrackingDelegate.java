package com.cabbage.mapinfoplayground;

import android.support.annotation.NonNull;
import android.view.View;

public abstract class TrackingDelegate {

    protected final TrackingActivity mTrackingActivity;

    public TrackingDelegate(@NonNull TrackingActivity activity) {
        this.mTrackingActivity = activity;
    }

    public abstract void callCompany(View v);
    public abstract void cancelTrip(View v);
    public abstract void showReceipt(View v);

    public abstract void startRefreshing();
    public abstract void stopRefreshing();
}
