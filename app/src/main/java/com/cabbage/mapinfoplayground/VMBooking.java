package com.cabbage.mapinfoplayground;

import android.content.Context;

public class VMBooking extends BaseViewModel<DBBooking> {

    public VMBooking(Context context, DBBooking dbBooking) {
        super(context, dbBooking);
    }

    public String getPickupAddress() {
        String houseNumber = mModel.getPickup_house_number();
        String streetName = mModel.getPickup_street_name();
        String landmark = mModel.getPickup_landmark();

        if (landmark != null) {
            return landmark;
        } else {
            if (houseNumber != null) {
                return houseNumber + streetName;
            } else {
                return streetName;
            }
        }
    }

    public String getDropoffAddress() {
        String houseNumber = mModel.getDropoff_house_number();
        String streetName = mModel.getDropoff_street_name();
        String landmark = mModel.getDropoff_landmark();

        if (landmark != null) {
            return landmark;
        } else {
            if (houseNumber != null) {
                return houseNumber + streetName;
            } else {
                return streetName;
            }
        }
    }

    public String getPickupTime() {
        if (mModel.getAsap()) {
            return "As soon as possible";
        } else {
            return mModel.getPickup_time();
        }
    }

    public String getPaymentMethod() {
        // TODO:
        return "ZoroPay";
    }

    public boolean getIsZoroPay() {
        return mModel.getZoroPayEnable();
    }

    public String getVehicleType() {
        
    }

}
