package com.dblab.domain;

import com.dblab.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@NoArgsConstructor
@Setter
@Getter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idx")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(length = 10000)
    private String profile;

    @Column(length = 1000)
    private String gitAddr;

    @Column(nullable = false)
    private LocalDateTime registeredDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Introduction> introductions = new HashSet<>();


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Project> projects;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<License> licenses;

    @Builder
    public User(String username, String password, String email, String profile, String gitAddr, LocalDateTime registeredDate,
                Set<Introduction> introductions, Set<Project> projects, Set<License> licenses) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profile = profile;
        this.gitAddr = gitAddr;
        this.registeredDate = registeredDate;
        this.introductions = introductions;
        this.projects = projects;
        this.licenses = licenses;
    }

    public void uploadImage(UserDto userDto) {
        this.profile = userDto.getProfile();
    }

    public void modifyUser(UserDto userDto, String uri) {
        this.username = userDto.getUsername();
        this.password = userDto.getPassword();
        this.email = userDto.getEmail();
        this.profile = uri;
        this.gitAddr = userDto.getGitAddr();
    }
}
