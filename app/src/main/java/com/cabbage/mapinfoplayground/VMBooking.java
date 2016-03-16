package com.cabbage.mapinfoplayground;

import android.content.Context;

public class VMBooking extends BaseViewModel {

    private DBBooking mDBBooking;

    public VMBooking(Context context, DBBooking dbBooking) {
        super(context);
        this.mDBBooking = dbBooking;
    }


}
