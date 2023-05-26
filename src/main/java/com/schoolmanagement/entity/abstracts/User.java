package com.schoolmanagement.entity.abstracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.schoolmanagement.entity.concretes.UserRole;
import com.schoolmanagement.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@MappedSuperclass // Db de user tablosu olusmadan bu sinifin anac sinif olarak kullanilmasini sagliyor
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder // Alt siniflarin USer sinifinin builder ozelliklerini kullanabilmesine izin verir
// !!! @SuperBuilder ile @Builder arasindaki temel fark :https://www.baeldung.com/lombok-builder-inheritance
// !!! @SuperBuilder in duzgun calismasi icin hem parent a hem de childa @SuperBuilder eklenmeli
public abstract class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String ssn;

    private String name;

    private String surname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

    private String birthPlace;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // hassas veri oldugu icin okuma islemlerinde kullanilmasin
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserRole userRole;

    private Gender gender;

}