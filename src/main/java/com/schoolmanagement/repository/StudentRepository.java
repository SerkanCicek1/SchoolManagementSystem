package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.payload.response.StudentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    Student findByUsernameEquals(String username);


    boolean existsByEmail(String email);

    @Query(value = "SELECT (count(s)>0) FROM Student s")
    boolean findStudent();

    @Query(value = "SELECT MAX(s.studentNumber) FROM Student s")
    int getMaxStudentNumber();

    List<Student> getStudentByNameContaining(String studentName);

    Optional<Student> findByUsername(String username);

    @Query(value = "SELECT s FROM Student s WHERE s.advisorTeacher.teacher.username =:username")
        // @Query(value= "SELECT s FROM Student s JOIN s.advisorTeacher at JOIN at.teacher t WHERE t.username=:username")
    List<Student> getStudentByAdvisorTeacher_Username(String username);

    @Query("SELECT s FROM Student s WHERE s.id IN :id")
    List<Student> findByIdsEquals(Long[] id);

}