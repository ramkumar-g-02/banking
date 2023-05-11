package com.banking.goal;

import com.banking.exception.BussinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

@Component
@EnableScheduling
public class GoalScheduler {

    @Autowired
    private GoalService goalService;

    @Scheduled(cron = "0 10 10 * * *")
    public void scheduleGoal() throws BussinessException {
        List<Goal> allGoals = goalService.findAllGoals();
        for (Goal goal : allGoals) {
            String frequency = goal.getFrequency();
            LocalDate today = LocalDate.now();
            int dayOfMonth = goal.getDayOfMonth();
            int dayOfWeek = goal.getDayOfWeek();
            boolean once = false;
            boolean weekly = false;
            boolean monthly = false;
            boolean daily = false;
            boolean isScheduledToday = false;
            if (today.equals(goal.getScheduledDate())) {
                isScheduledToday = true;
            }
            if (GoalFrequency.ONCE.toString().equalsIgnoreCase(frequency)) {
                once = true;
            }
            if (GoalFrequency.WEEKLY.toString().equalsIgnoreCase(frequency) && dayOfWeek == today.getDayOfWeek().getValue() && !once && !isScheduledToday) {
                weekly = true;
            }
            if (GoalFrequency.MONTHLY.toString().equalsIgnoreCase(frequency) && dayOfMonth == today.getDayOfMonth() && !once && !isScheduledToday) {
                monthly = true;
            }
            if (GoalFrequency.DAILY.toString().equalsIgnoreCase(frequency) && !once && !isScheduledToday) {
                daily = true;
            }
            if (daily || weekly || monthly) {
                goal.setScheduledDate(LocalDate.now());
                goalService.addGoal(goal);
            }
        }
    }

    public void scheduleGoal1() throws BussinessException {
        List<Goal> allGoals = goalService.findAllGoals();
        Predicate<Goal> todayGoalFilter = goal -> {
            String frequency = goal.getFrequency();
            LocalDate today = LocalDate.now();
            int dayOfMonth = goal.getDayOfMonth();
            int dayOfWeek = goal.getDayOfWeek();
            boolean once = GoalFrequency.ONCE.toString().equalsIgnoreCase(frequency);
            boolean weekly = GoalFrequency.WEEKLY.toString().equalsIgnoreCase(frequency) && dayOfWeek == today.getDayOfWeek().getValue() && !once;
            boolean monthly = GoalFrequency.MONTHLY.toString().equalsIgnoreCase(frequency) && dayOfMonth == today.getDayOfMonth()  && !once;
            boolean daily = GoalFrequency.DAILY.toString().equalsIgnoreCase(frequency) && !once;
            return daily || weekly || monthly;
        };
        List<Goal> todayGoals = allGoals.stream().filter(todayGoalFilter).toList();
        todayGoals.forEach(goalService::addGoal);
    }


}


