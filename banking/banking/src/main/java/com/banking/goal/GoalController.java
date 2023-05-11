package com.banking.goal;

import com.banking.exception.BussinessException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/goal/")
public class GoalController {
    @Autowired
    private GoalService goalService;
    @Autowired
    private GoalSchedulingConfig goalSchedulingConfig;
    @Autowired
    private GoalScheduler goalScheduler;

    @PostMapping("/add")
    public ResponseEntity<Goal> addGoal(@Valid @RequestBody GoalDTO goalDTO) throws BussinessException {
        Goal goal = goalService.addGoal(goalDTO);
        return new ResponseEntity<>(goal, HttpStatus.OK);
    }

    @GetMapping("/add-scheduler")
    public ResponseEntity<String> addGoalWithScheduler() throws BussinessException {
        goalScheduler.scheduleGoal();
//        goalSchedulingConfig.addGoal(goalDTO);
        return new ResponseEntity<>("Goal scheduled", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Goal>> findAllGoals(@RequestParam String accountNumber) throws BussinessException {
        List<Goal> goals = goalService.findAllGoals(Long.parseLong(accountNumber));
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @PostMapping("/release")
    public ResponseEntity<Goal> releaseGoal(@Valid @RequestBody GoalDTO goalDTO) throws BussinessException {
        Goal goal = goalService.releaseGoal(goalDTO);
        return new ResponseEntity<>(goal, HttpStatus.OK);
    }

    @GetMapping("/rest")
    public ResponseEntity<String> rest() throws BussinessException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8081/sample/h", String.class);
        return new ResponseEntity<>(entity.getBody(), HttpStatus.OK);
    }

}
