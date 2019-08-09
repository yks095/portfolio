package com.dblab.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private LocalDateTime registeredDate;

    @Builder
    public User(String username, String password, String email, LocalDateTime registeredDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.registeredDate = registeredDate;
    }
}
