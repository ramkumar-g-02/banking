package com.ecommerce.order.entity;

public enum PaymentMethod {
    PAY_ON_DELEIVERY{
        @Override
        public String toString() {
            return "Cash On Delivery";
        }
    },

    PAYTM,

    GPAY
}
