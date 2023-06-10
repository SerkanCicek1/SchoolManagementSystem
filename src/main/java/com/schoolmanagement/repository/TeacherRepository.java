package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.Teacher;
import com.schoolmanagement.payload.response.TeacherResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    Teacher findByUsernameEquals(String username);

    boolean existsByEmail(String email);

    List<Teacher> getTeacherByNameContaining(String teacherName);

}