package com.dblab.dto;

import com.dblab.domain.Introduction;
import com.dblab.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class IntroductionDto {

    @NotBlank(message = "필수 항목입니다.")
    private String title;

    private String growth;

    private String reason;

    private String strength;

    private String weakness;

    private String aspiration;

    private LocalDateTime registeredDate;

    private User user;

    public Introduction setIntroduction(User currentUser) {
        Introduction introduction = new Introduction();

        introduction.setTitle(this.title);
        introduction.setGrowth(this.growth);
        introduction.setReason(this.reason);
        introduction.setStrength(this.strength);
        introduction.setWeakness(this.weakness);
        introduction.setAspiration(this.aspiration);
        introduction.setRegisteredDate(LocalDateTime.now());
        introduction.setUsers(currentUser);

        return introduction;
    }
}
