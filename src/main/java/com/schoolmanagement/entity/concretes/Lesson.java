package com.schoolmanagement.entity.concretes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    private String lessonName ;

    private Integer creditScore ;

    private Boolean isCompulsory; // bu ders zorunlu mu ??

    @ManyToMany(mappedBy = "lesson")
    private Set<LessonProgram> lessonPrograms;
}