package com.banking.goal;

public enum GoalServiceType {
    ADD_GOAL{
        @Override
        public String toString() {
            return "Add";
        }
    },

    RELEASE_GOAL {
        @Override
        public String toString() {
            return "Release";
        }
    }
}
