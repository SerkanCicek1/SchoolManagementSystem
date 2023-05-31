package com.schoolmanagement;

import com.schoolmanagement.entity.enums.Gender;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.payload.request.AdminRequest;
import com.schoolmanagement.service.AdminService;
import com.schoolmanagement.service.UserRoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class SchoolManagementApplication implements CommandLineRunner {

	private final UserRoleService userRoleService;

	private final AdminService adminService;

	public SchoolManagementApplication(UserRoleService userRoleService, AdminService adminService) {
		this.userRoleService = userRoleService;
		this.adminService = adminService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SchoolManagementApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// !!! Role tablomu dolduracagim
		if(userRoleService.getAllUserRole().size()==0) {
			userRoleService.save(RoleType.ADMIN);
			userRoleService.save(RoleType.MANAGER);
			userRoleService.save(RoleType.ASSISTANTMANAGER);
			userRoleService.save(RoleType.TEACHER);
			userRoleService.save(RoleType.STUDENT);
			userRoleService.save(RoleType.ADVISORTEACHER);
			userRoleService.save(RoleType.GUESTUSER);
		}

		//!!! Admin olusturulacak  built_in
		if(adminService.countAllAdmin()==0) {
			AdminRequest admin = new AdminRequest();
			admin.setUsername("Admin");
			admin.setSsn("987-99-9999");
			admin.setPassword("12345678");
			admin.setName("Admin");
			admin.setSurname("Admin");
			admin.setPhoneNumber("555-444-4321");
			admin.setGender(Gender.FEMALE);
			admin.setBirthDay(LocalDate.of(2002,2,2));
			admin.setBirthPlace("US");
			adminService.save(admin);
		}

	}
}