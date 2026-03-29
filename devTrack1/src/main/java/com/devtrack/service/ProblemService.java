package com.devtrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devtrack.model.Problem;
import com.devtrack.repository.ProblemRepository;

@Service
public class ProblemService {

    @Autowired
    private ProblemRepository repository;

    @Autowired
    private GeminiService geminiService;

    // Save problem
    public Problem saveProblem(Problem problem) {
        return repository.save(problem);
    }

    // Get all problems
    public List<Problem> getAllProblems() {
        return repository.findAll();
    }

    // 🧠 AI Analysis (SAFE VERSION — WILL NOT BREAK APP)
    public String analyzeSkills() {

        List<Problem> problems = repository.findAll();

        int easy = 0, medium = 0, hard = 0;

        for (Problem p : problems) {
            if (p.getDifficulty().equalsIgnoreCase("Easy")) easy++;
            else if (p.getDifficulty().equalsIgnoreCase("Medium")) medium++;
            else if (p.getDifficulty().equalsIgnoreCase("Hard")) hard++;
        }

        if (problems.isEmpty()) {
            return "Start solving problems to see your skill analysis.";
        }

        String prompt = "You are a DSA coach.\n\n" +
                "Student data:\n" +
                "Easy: " + easy + ", Medium: " + medium + ", Hard: " + hard + "\n" +
                "Total problems: " + problems.size() + "\n" +
                "Topics practiced: " + problems.stream().map(p -> p.getTopic()).distinct().toList() + "\n" +
                "Struggle levels: " + problems.stream().map(p -> p.getStruggleLevel()).toList() + "\n\n" +
                "Give specific improvement advice. Avoid generic programming tips.";

        try {
            String ai = geminiService.analyze(prompt);

            if (ai == null || ai.trim().isEmpty()) {
                return "AI returned empty response. Keep practicing!";
            }

            return ai;

        } catch (Exception e) {
            return "AI temporarily unavailable. Keep solving problems!";
        }
    }

    // 📊 Stats
    public int getEasyCount() {
        return (int) repository.findAll().stream()
                .filter(p -> p.getDifficulty().equalsIgnoreCase("Easy"))
                .count();
    }

    public int getMediumCount() {
        return (int) repository.findAll().stream()
                .filter(p -> p.getDifficulty().equalsIgnoreCase("Medium"))
                .count();
    }

    public int getHardCount() {
        return (int) repository.findAll().stream()
                .filter(p -> p.getDifficulty().equalsIgnoreCase("Hard"))
                .count();
    }

    // 📅 Total unique coding days
    public long getTotalDaysCoded() {
        return repository.findAll().stream()
                .map(p -> p.getDate())
                .distinct()
                .count();
    }

    // 📅 All dates (for calendar)
    public List<String> getAllDates() {
        return repository.findAll().stream()
                .map(p -> p.getDate())
                .distinct()
                .toList();
    }

    // 🔥 Streak logic
    public int getStreak() {
        List<String> dates = repository.findAll().stream()
                .map(p -> p.getDate())
                .distinct()
                .sorted()
                .toList();

        if (dates.isEmpty()) return 0;

        int streak = 1;

        for (int i = dates.size() - 1; i > 0; i--) {

            String current = dates.get(i);
            String previous = dates.get(i - 1);

            int currDay = Integer.parseInt(current.substring(8));
            int prevDay = Integer.parseInt(previous.substring(8));

            if (currDay - prevDay == 1) {
                streak++;
            } else {
                break;
            }
        }

        return streak;
    }

	public GeminiService getGeminiService() {
		return geminiService;
	}

	public void setGeminiService(GeminiService geminiService) {
		this.geminiService = geminiService;
	}
}