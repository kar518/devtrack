package com.devtrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.devtrack.model.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}