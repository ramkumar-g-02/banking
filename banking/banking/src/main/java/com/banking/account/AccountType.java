package com.banking.account;

public enum AccountType {

    SAVINGS {
        @Override
        public String toString() {
            return "Savings";
        }
    },

    CURRENT {
        @Override
        public String toString() {
            return "Current";
        }
    }

}
