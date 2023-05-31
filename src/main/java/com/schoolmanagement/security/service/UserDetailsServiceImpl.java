package com.schoolmanagement.security.service;

import com.schoolmanagement.entity.concretes.*;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DeanRepository deanRepository;
    private final ViceDeanRepository viceDeanRepository;
    private final AdminRepository adminRepository;

    @Override
    @Transactional // eklenecek
    /*
        Bu özel durumda, loadUserByUsername methodu kullanıcının veritabanından bilgilerini yüklemek
        için farklı repository'leri kullanır. Bu repository'lerden her biri ayrı bir veritabanı işlemi
        gerçekleştirir. @Transactional annotasyonu, tüm bu işlemlerin tek bir transaction içinde
        gerçekleştirilmesini sağlar. Yani, eğer herhangi bir veritabanı işlemi başarısız olursa,
        tüm işlemler geri alınır (rollback) ve veritabanı tutarlı bir durumda kalır.
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Student student = studentRepository.findByUsernameEquals(username);
        Teacher teacher = teacherRepository.findByUsernameEquals(username);
        Dean dean = deanRepository.findByUsernameEquals(username);
        ViceDean viceDean = viceDeanRepository.findByUsernameEquals(username);
        Admin admin = adminRepository.findByUsernameEquals(username);

        if(student != null) {
            return new UserDetailsImpl(student.getId(),
                    student.getUsername(),
                    student.getName(),
                    false,
                    student.getPassword(),
                    student.getUserRole().getRoleType().name()
            );
        } else if(teacher!=null){
            return new UserDetailsImpl(teacher.getId(),
                    teacher.getUsername(),
                    teacher.getName(),
                    teacher.getIsAdvisor(),
                    teacher.getPassword(),
                    teacher.getUserRole().getRoleType().name());
        } else if(dean != null) {
            return new UserDetailsImpl(dean.getId(),
                    dean.getUsername(),
                    dean.getName(),
                    false,
                    dean.getPassword(),
                    dean.getUserRole().getRoleType().name());

        }else if(viceDean != null) {
            return new UserDetailsImpl(viceDean.getId(),
                    viceDean.getUsername(),
                    viceDean.getName(),
                    false,
                    viceDean.getPassword(),
                    viceDean.getUserRole().getRoleType().name());
        }else if(admin != null) {
            return new UserDetailsImpl(admin.getId(),
                    admin.getUsername(),
                    admin.getName(),
                    false,
                    admin.getPassword(),
                    admin.getUserRole().getRoleType().name()); //  RoleType.ADMIN.name()
        }

        // TODO --> Security katmani icin genel exception handle class olusturulacak
        // throw new ConflictException("no User");
        throw new UsernameNotFoundException("User '" + username + "' not found");
    }
}