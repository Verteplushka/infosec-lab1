package com.InformationSecurity.Lab1.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "infosec_users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
}
