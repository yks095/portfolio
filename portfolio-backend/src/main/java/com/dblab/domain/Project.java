package com.dblab.domain;

import com.dblab.dto.ProjectDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table
@Getter
@Setter
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String name;

    @Column
    private String period;

    @Column
    private String persons;

    @Column
    private String description;

    @Column
    private LocalDateTime registeredDate;

    @ManyToOne
    private User user;

    @Builder
    public Project(String name, String period, String persons, String description, LocalDateTime registeredDate) {
        this.name = name;
        this.period = period;
        this.persons = persons;
        this.description = description;
        this.registeredDate = registeredDate;
    }


    public void modifyProject(ProjectDto projectDto) {
        this.name = projectDto.getName();
        this.period = projectDto.getPeriod();
        this.persons = projectDto.getPersons();
        this.description = projectDto.getDescription();
    }

    public void setUsers(User currentUser) {
        if(this.user != null) this.user.getProjects().remove(this);

        this.user = currentUser;
        currentUser.getProjects().add(this);
    }
}
