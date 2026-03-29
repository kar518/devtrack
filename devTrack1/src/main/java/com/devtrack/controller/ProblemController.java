package com.devtrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.devtrack.model.Problem;
import com.devtrack.service.ProblemService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ProblemController {

    @Autowired
    private ProblemService service;

    @GetMapping("/problems")
    public java.util.List<Problem> getProblems() {
        return service.getAllProblems();
    }

    @GetMapping("/analytics")
    public java.util.Map<String, Object> getAnalytics() {

        java.util.Map<String, Object> data = new java.util.HashMap<>();

        data.put("problems", service.getAllProblems());
        data.put("easy", service.getEasyCount());
        data.put("medium", service.getMediumCount());
        data.put("hard", service.getHardCount());
        data.put("days", service.getTotalDaysCoded());
        data.put("analysis", service.analyzeSkills());
        data.put("streak", service.getStreak());
        data.put("dates", service.getAllDates());

        return data;
    }

    @PostMapping("/add")
    public Problem addProblem(@RequestBody Problem problem) {
        return service.saveProblem(problem);
    }
}