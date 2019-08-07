package com.dblab.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table
@Getter
@Setter
public class Introduction {

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

    @Builder
    public Introduction(String title, String growth, String reason, String strength, String weakness, String aspiration, LocalDateTime registeredDate) {
        this.title = title;
        this.growth = growth;
        this.reason = reason;
        this.strength = strength;
        this.weakness = weakness;
        this.aspiration = aspiration;
        this.registeredDate = registeredDate;
    }

    public void modiIntroduction(Introduction introduction) {
        this.title = introduction.getTitle();
        this.growth = introduction.getGrowth();
        this.reason = introduction.getReason();
        this.strength = introduction.getStrength();
        this.weakness = introduction.getWeakness();
        this.aspiration = introduction.getAspiration();
    }
}
