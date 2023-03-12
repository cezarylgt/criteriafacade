package com.cezarylgt.java.criteriafacade.demoapp;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Person {
    @Id
    private Integer id;
    private String name;
    private Integer age;
    private boolean enabled;
    private LocalDate dateOfBirth;
    private LocalDateTime registrationTime;
}
