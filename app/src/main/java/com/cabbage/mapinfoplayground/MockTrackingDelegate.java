package com.cabbage.mapinfoplayground;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MockTrackingDelegate extends TrackingDelegate {
    private final static int INTERVAL = 1000 * 10; // 30 second

    private Subscription automateSubscription;

    public MockTrackingDelegate(@NonNull TrackingActivity activity) {
        super(activity);
    }

    @Override
    public void callCompany(View v) {
        sieg("Mock call company");
    }

    @Override
    public void showReceipt(View v) {
        sieg("Mock show Receipt");
    }

    @Override
    public void cancelTrip(View v) {
        sieg("Mock cancel Trip");
    }

    @Override
    public void startRefreshing() {
        automateSubscription = Observable.interval(0, INTERVAL, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Long l) -> {
                    sieg("refresh" + l);
                    int code = l.intValue();
                    if (code < 5) {
                        mTrackingActivity.setTripStage(TripStatus.values()[code]);
                    }
                });
    }

    @Override
    public void stopRefreshing() {
        if (automateSubscription != null && !automateSubscription.isUnsubscribed()) {
            automateSubscription.unsubscribe();
        }
    }

    private void sieg(String str) {
        Toast.makeText(mTrackingActivity, str, Toast.LENGTH_SHORT).show();
    }
}
