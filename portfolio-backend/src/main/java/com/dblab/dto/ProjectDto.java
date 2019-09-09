package com.dblab.dto;

import com.dblab.domain.Project;
import com.dblab.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDto {

    @NotBlank(message = "필수 항목입니다.")
    private String name;

    private String period;

    private String persons;

    private String description;

    private LocalDateTime registeredDate;

    private User user;

    public Project save(User user)  {
        Project project = new Project();

        project.setName(this.name);
        project.setPeriod(this.period);
        project.setPersons(this.persons);
        project.setDescription(this.description);
        project.setRegisteredDate(LocalDateTime.now());
        project.setUsers(user);

        return project;
    }

}
