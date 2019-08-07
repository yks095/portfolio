package com.dblab.domain;

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

    @Builder
    public Project(String name, String period, String persons, String description, LocalDateTime registeredDate) {
        this.name = name;
        this.period = period;
        this.persons = persons;
        this.description = description;
        this.registeredDate = registeredDate;
    }


    public void modifyProject(Project project) {
        this.name = project.getName();
        this.period = project.getPeriod();
        this.persons = project.getPersons();
        this.description = project.getDescription();
    }
}
