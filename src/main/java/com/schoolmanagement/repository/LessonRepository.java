package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    boolean existsLessonByLessonNameEqualsIgnoreCase(String lessonName);

    Optional<Lesson> getLessonByLessonName(String lessonName);

    @Query(value = "SELECT l FROM Lesson l WHERE l.lessonId IN :lessons")
    Set<Lesson> getLessonByLessonIdList(Set<Long> lessons);

    boolean existsByLessonIdEquals(Long lessonId);

    Lesson findByLessonIdEquals(Long lessonId);
}