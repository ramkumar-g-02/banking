package com.banking.goal;

import com.banking.exception.BussinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Component
@EnableScheduling
public class GoalSchedulingConfig {
    @Autowired
    private GoalServiceExecutor goalServiceExecutor;
    @Autowired
    private TaskScheduler taskScheduler;

    public void addGoal(GoalDTO goalDTO) throws BussinessException {
        CronExpression cronExpression = getCronExpression(goalDTO);
        goalDTO.setServiceType(GoalServiceType.ADD_GOAL.toString());
        goalServiceExecutor.setGoalDTO(goalDTO);
        System.out.println(cronExpression.getExpression());
        taskScheduler.schedule(
                goalServiceExecutor,
                new CronTrigger(cronExpression.getExpression())
        );
    }

    private CronExpression getCronExpression(GoalDTO goalDTO) throws BussinessException {
        CronExpression cronExpression = new CronExpression();
        boolean hasFrequency = StringUtils.hasLength(goalDTO.getFrequency());
        if (hasFrequency) {
            if (GoalFrequency.DAILY.toString().equalsIgnoreCase(goalDTO.getFrequency())) {
                LocalDateTime time = LocalDateTime.now();
                cronExpression.setDaily(time.getHour(), time.getMinute(), time.getSecond());
            } else if (GoalFrequency.WEEKLY.toString().equalsIgnoreCase(goalDTO.getFrequency())) {
                boolean hasDayOfWeek = StringUtils.hasLength(goalDTO.getDayOfWeek());
                Optional<DayOfWeek> dayOfWeek = Arrays.stream(DayOfWeek.values()).filter(day -> day.toString().equalsIgnoreCase(goalDTO.getDayOfWeek())).findFirst();
                if (hasDayOfWeek && dayOfWeek.isPresent()) {
                    LocalDateTime time = LocalDateTime.now();
                    cronExpression.setDaily(time.getHour(), time.getMinute(), time.getSecond());
                    cronExpression.setWeekly(String.valueOf(dayOfWeek.get().getValue() % 7));
                } else {
                    throw new BussinessException("Invalid Day Of Week");
                }
            } else if (GoalFrequency.MONTHLY.toString().equalsIgnoreCase(goalDTO.getFrequency())) {
                LocalDateTime time = LocalDateTime.now();
                cronExpression.setDaily(time.getHour(), time.getMinute(), time.getSecond());
                cronExpression.setMonthly(String.valueOf(goalDTO.getDayOfMonth().getDayOfMonth()));
            }
        } else {
            throw new BussinessException("Frequency should not be empty");
        }
        return cronExpression;
    }

}
