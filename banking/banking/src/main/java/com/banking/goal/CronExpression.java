package com.banking.goal;

public class CronExpression {

    private String expression = "* * * * * * ";

    public void setMonthly(String dayOfMonth) {
        String[] cron = expression.split(" ");
        cron[3] = dayOfMonth;
        cron[4] = "* ";
        this.expression = String.join(" ", cron);
    }

    public void setWeekly(String day) {
        String[] cron = expression.split(" ");
        cron[5] = day;
        cron[4] = "* ";
        this.expression = String.join(" ", cron);
    }

    public void setDaily(int hour, int min, int sec) {
        String[] cron = expression.split(" ");
        cron[0] = "1,10" + "";
//        cron[1] = min + "";
//        cron[0] = sec + "";
        cron[1] = "* ";
        cron[2] = hour + "";
        this.expression = String.join(" ", cron);
    }

    public String getExpression() {
        return expression;
    }
}
