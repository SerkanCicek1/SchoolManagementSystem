package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.Meet;
import com.schoolmanagement.payload.response.MeetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetRepository extends JpaRepository<Meet, Long> {
    List<Meet> findByStudentList_IdEquals(Long studentId);

    Page<Meet> findByAdvisorTeacher_IdEquals(Long id, Pageable pageable);

    List<Meet> getByAdvisorTeacher_IdEquals(Long id);

}