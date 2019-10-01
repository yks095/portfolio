package com.dblab.dto;

import com.dblab.domain.Project;
import com.dblab.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDto {

    @NotBlank(message = "필수 항목입니다.")
    private String name;

    private String period;

    private String description;

    private String image;

    @URL(message = "URL 양식을 지켜주세요.")
    private String gitAddr;

    private LocalDateTime registeredDate;

    private User user;

    public Project save(User user, String uri)  {
        Project project = new Project();

        project.setName(this.name);
        project.setPeriod(this.period);
        project.setDescription(this.description);
        project.setImage(uri);
        project.setGitAddr(this.gitAddr);
        project.setRegisteredDate(LocalDateTime.now());
        project.setUsers(user);

        return project;
    }

}
