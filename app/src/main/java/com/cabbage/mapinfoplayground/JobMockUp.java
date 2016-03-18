package com.cabbage.mapinfoplayground;

import java.util.Random;

public class JobMockUp {

    public static DBBooking getMockBooking() {
        DBBooking job = new DBBooking();
        int rnd = new Random().nextInt(3);
//        int rnd = 0;
        if (rnd == 0) {
            job.setPickup_landmark("Burger town");
        } else if (rnd == 1) {
            job.setPickup_house_number("8080");
            job.setPickup_street_name("Left hand St.");
        } else {
            job.setPickup_street_name("Blue bird highway");
        }

        if (new Random().nextBoolean()) {
            job.setDropoff_house_number("5678");
            job.setDropoff_street_name("Burger Ave.");
        }

        job.setAsap(false);
        job.setPickup_time("Future time");

        job.setZoroPayEnable(true);

        job.setCompany_name("Cab from hell");
        job.setRemarks("Get me a coke!");

        job.setTripStatus(0);

        return job;
    }
}
