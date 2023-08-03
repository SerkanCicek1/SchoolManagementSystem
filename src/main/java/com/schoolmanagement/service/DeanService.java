package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.Dean;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.dto.DeanDto;
import com.schoolmanagement.payload.request.DeanRequest;
import com.schoolmanagement.payload.response.DeanResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.DeanRepository;
import com.schoolmanagement.utils.CheckParameterUpdateMethod;
import com.schoolmanagement.utils.FieldControl;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeanService {

    private final DeanRepository deanRepository;
    private final DeanDto deanDto;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final FieldControl fieldControl;


    // Not: Save() *************************************************
    public ResponseMessage<DeanResponse> save(DeanRequest deanRequest) {

        //!!! Dublicate kontrolu
        fieldControl.checkDuplicate(deanRequest.getUsername(),
                deanRequest.getSsn(),
                deanRequest.getPhoneNumber());

        // !!! DTO -POJO donusumu
        Dean dean = createDtoForDean(deanRequest);
        // !!! role ve password bilgileri uygun sekilde setleniyor
        dean.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
        dean.setPassword(passwordEncoder.encode(dean.getPassword()));

        // !!! Db ye kayit
        Dean savedDean = deanRepository.save(dean);

        return ResponseMessage.<DeanResponse>builder()
                .message("Dean Saved")
                .httpStatus(HttpStatus.CREATED)
                .object(createDeanResponse(savedDean))
                .build();
        // yardimci metod

    }

    private Dean createDtoForDean(DeanRequest deanRequest){ // createDtoToPOJO

        return deanDto.dtoDean(deanRequest);
    }

    private DeanResponse createDeanResponse(Dean dean){
        return DeanResponse.builder()
                .userId(dean.getId())
                .username(dean.getUsername())
                .name(dean.getName())
                .surname(dean.getSurname())
                .birthDay(dean.getBirthDay())
                .birthPlace(dean.getBirthPlace())
                .phoneNumber(dean.getPhoneNumber())
                .gender(dean.getGender())
                .ssn(dean.getSsn())
                .build();
    }

    // Not :  UpdateById() **********************************************
    public ResponseMessage<DeanResponse> update(DeanRequest newDean, Long deanId) {

        //  checkDeanExists(deanId);  // tekrarlanan kisim icin

        Optional<Dean> dean = deanRepository.findById(deanId);

        // dean objesi bos olma kontrolu
        if(!dean.isPresent()) { // isEmpty() de kullanilabilir

            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, deanId));
        } else if(!CheckParameterUpdateMethod.checkParameter(dean.get(),newDean)) {

            // tek parametre degistirildiginde senaryo postmande test edilmeli
            fieldControl.checkDuplicate(newDean.getUsername(),newDean.getSsn(), newDean.getPhoneNumber());
        }

        // !!! guncellenen yeni bilgiler ile Dean objesini kaydediyoruz
        Dean updatedDean = createUpdatedDean(newDean,deanId);
        updatedDean.setPassword(passwordEncoder.encode(newDean.getPassword()));
        deanRepository.save(updatedDean);

        return ResponseMessage.<DeanResponse>builder()
                .message("Dean Updated Successfully")
                .httpStatus(HttpStatus.OK)
                .object(createDeanResponse(updatedDean))
                .build();

    }

    //!!! yardimci metod
    private Dean createUpdatedDean(DeanRequest deanRequest, Long managerId) {

        return Dean.builder()
                .id(managerId)
                .username(deanRequest.getUsername())
                .ssn(deanRequest.getSsn())
                .name(deanRequest.getName())
                .surname(deanRequest.getSurname())
                .birthPlace(deanRequest.getBirthPlace())
                .birthDay(deanRequest.getBirthDay())
                .phoneNumber(deanRequest.getPhoneNumber())
                .gender(deanRequest.getGender())
                .userRole(userRoleService.getUserRole(RoleType.MANAGER))
                .build();
    }

    // Not :  Delete() ****************************************************
    public ResponseMessage<?> deleteDean(Long deanId) {

        checkDeanExists(deanId);  // tekrarlanan kisim icin

/*        Optional<Dean> dean = deanRepository.findById(deanId);

        if(!dean.isPresent()) { // isEmpty() de kullanilabilir

            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, deanId));
        }*/

        deanRepository.deleteById(deanId);

        return ResponseMessage.builder()
                .message("Dean Deleted")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Not :  getById() ************************************************************************
    public ResponseMessage<DeanResponse> getDeanById(Long deanId) {


        checkDeanExists(deanId);  // tekrarlanan kisim icin

/*        Optional<Dean> dean = deanRepository.findById(deanId);

        if(!dean.isPresent()) { // isEmpty() de kullanilabilir

            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, deanId));
        }*/

        return ResponseMessage.<DeanResponse>builder()
                .message("Dean Successfully found")
                .httpStatus(HttpStatus.OK)
                .object(createDeanResponse(checkDeanExists(deanId).get()))
                .build();

    }

    // Not :  getAll() *************************************************************************
    public List<DeanResponse> getAllDean() {

        return deanRepository.findAll()
                .stream()
                .map(this::createDeanResponse)
                .collect(Collectors.toList());
    }

    // Not :  Search() *************************************************************************
    public Page<DeanResponse> search(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());
        if(Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page,size,Sort.by(sort).descending());
        }

        return deanRepository.findAll(pageable).map(this::createDeanResponse);

    }

    // Not: tekrarlanan kod blogu icin yazilan method
    private Optional<Dean> checkDeanExists(Long deanId) {
        Optional<Dean> dean = deanRepository.findById(deanId);
        if (!dean.isPresent()) {
            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, deanId));
        }
        return dean;
    }
}
