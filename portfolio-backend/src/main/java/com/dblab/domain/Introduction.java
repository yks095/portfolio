package com.dblab.domain;

import com.dblab.dto.IntroductionDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table
@Getter
@Setter
public class Introduction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String title;

    //성장배경
    @Column
    private String growth;

    //지원동기
    @Column
    private String reason;

    //장점
    @Column
    private String strength;

    //단점
    @Column
    private String weakness;

    //입사 후 포부
    @Column
    private String aspiration;

    @Column
    private LocalDateTime registeredDate;

    @ManyToOne
    private User user;

    @Builder
    public Introduction(String title, String growth, String reason, String strength, String weakness, String aspiration, LocalDateTime registeredDate,
                        User user) {
        this.title = title;
        this.growth = growth;
        this.reason = reason;
        this.strength = strength;
        this.weakness = weakness;
        this.aspiration = aspiration;
        this.registeredDate = registeredDate;
        this.user = user;
    }

    public void modifyIntroduction(IntroductionDto introductionDto) {
        this.title = introductionDto.getTitle();
        this.growth = introductionDto.getGrowth();
        this.reason = introductionDto.getReason();
        this.strength = introductionDto.getStrength();
        this.weakness = introductionDto.getWeakness();
        this.aspiration = introductionDto.getAspiration();
    }

    public void setUsers(User currentUser) {
        if(this.user != null) this.user.getIntroductions().remove(this);

        this.user = currentUser;
        currentUser.getIntroductions().add(this);
    }
}
