package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.Admin;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.payload.request.AdminRequest;
import com.schoolmanagement.payload.response.AdminResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.*;
import com.schoolmanagement.utils.FieldControl;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final ViceDeanRepository viceDeanRepository;
    private final DeanRepository deanRepository;
    private final TeacherRepository teacherRepository;
    private final GuestUserRepository guestUserRepository;
    private final FieldControl fieldControl;


    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    // Not: save()  *******************************************************
    public ResponseMessage save(AdminRequest request) {

        // !!! Girilen username - ssn- phoneNumber unique mi kontrolu
        fieldControl.checkDuplicate(request.getUsername(), request.getSsn(), request.getPhoneNumber());
        //checkDuplicate(request.getUsername(), request.getSsn(), request.getPhoneNumber());
        // !!! Admin nesnesi builder ile olusturalim
        Admin admin = createAdminForSave(request);
        admin.setBuilt_in(false);

        if(Objects.equals(request.getUsername(), "Admin")) admin.setBuilt_in(true);

        // !!! admin rolu veriliyor
        admin.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));
        //!!! password encode ediliyor
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        Admin savedDate = adminRepository.save(admin);

        return ResponseMessage.<AdminResponse>builder()
                .message("Admin saved")
                .httpStatus(HttpStatus.CREATED)
                .object(createResponse(savedDate)) // pojo- dto
                .build();

    }



    protected Admin createAdminForSave(AdminRequest request){

        return Admin.builder().
                username(request.getUsername()).
                name(request.getName())
                .surname(request.getSurname())
                .password(request.getPassword())
                .ssn(request.getSsn())
                .birthDay(request.getBirthDay())
                .birthPlace(request.getBirthPlace())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .build();
    }

    private AdminResponse createResponse(Admin admin){

        return AdminResponse.builder()
                .userId(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .surname(admin.getSurname())
                .phoneNumber(admin.getPhoneNumber())
                .gender(admin.getGender())
                .ssn(admin.getSsn())
                .build();
    }

    // Not: getALL()********************************************************
    public Page<Admin> getAllAdmin(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    // Not: delete() *******************************************************
    public String deleteAdmin(Long id) {

        Optional<Admin> admin = adminRepository.findById(id);

        if(admin.isPresent() && admin.get().isBuilt_in()) {
            throw new ConflictException(Messages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        if(admin.isPresent()) {
            adminRepository.deleteById(id);

            return "Admin is deleted Successfully";
        }

        return Messages.NOT_FOUND_USER_MESSAGE;
    }

    // !!! Runner tarafi icin yazildi
    public long countAllAdmin() {

        return adminRepository.count();
    }
}