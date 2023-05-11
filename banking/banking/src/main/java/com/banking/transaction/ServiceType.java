package com.banking.transaction;

public enum ServiceType {

    ATM_TRANSACTION("ATM"),

    UPI_TRANSACTION("UPI"),

    GOAL_TRANSACTION("GOAL");

    private final String serviceType;

    ServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceType() {
        return serviceType;
    }
}
