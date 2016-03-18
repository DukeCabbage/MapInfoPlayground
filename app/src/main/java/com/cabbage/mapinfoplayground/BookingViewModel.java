package com.cabbage.mapinfoplayground;

import android.content.Context;
import android.text.TextUtils;

public class BookingViewModel extends BaseViewModel<DBBooking> {

    public BookingViewModel(Context context, DBBooking dbBooking) {
        super(context, dbBooking);
    }

    public String getPickupAddress() {
        String houseNumber = mModel.getPickup_house_number();
        String streetName = mModel.getPickup_street_name();
        String landmark = mModel.getPickup_landmark();

        if (!TextUtils.isEmpty(landmark)) {
            return landmark;
        } else {
            if (!TextUtils.isEmpty(houseNumber)) {
                return houseNumber + " " + streetName;
            } else {
                return streetName;
            }
        }
    }

    public String getDropoffAddress() {
        String houseNumber = mModel.getDropoff_house_number();
        String streetName = mModel.getDropoff_street_name();
        String landmark = mModel.getDropoff_landmark();

        if (!TextUtils.isEmpty(landmark)) {
            return landmark;
        } else {
            if (!TextUtils.isEmpty(houseNumber)) {
                return houseNumber + " " + streetName;
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
        return "ZoroPay(placeholder)";
    }

    public boolean getIsZoroPay() {
        return mModel.getZoroPayEnable();
    }

    public String getVehicleType() {
        return "Sedan(placeholder)";
    }

    public String getCompanyName() {
        return mModel.getCompany_name();
    }

    public String getCarNumber() {
        String carNum = mModel.getDispatchedCar();
        if (TextUtils.isEmpty(carNum) || carNum.equalsIgnoreCase("0")) {
            return "";
        } else {
            return "#" + carNum;
        }
    }

    public String getDriverNote() {
        String driverNote = mModel.getRemarks();
        if (driverNote.length() > 24) {
            driverNote = driverNote.substring(0, 24);
            driverNote += "...";
        }

        return ("\"" + driverNote + "\"");
    }
}
