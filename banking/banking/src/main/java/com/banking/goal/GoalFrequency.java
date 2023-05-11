package com.banking.goal;

public enum GoalFrequency {

    ONCE {
        @Override
        public String toString() {
            return "Once";
        }
    },

    DAILY {
        @Override
        public String toString() {
            return "Daily";
        }
    },

    WEEKLY {
        @Override
        public String toString() {
            return "Weekly";
        }
    },

    MONTHLY {
        @Override
        public String toString() {
            return "Monthly";
        }
    }

}
