package com.dblab.domain;

import com.dblab.dto.IntroductionDto;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Getter @Setter
public class Introduction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String introductionTitle;

    @Column
    private String title1;

    @Column
    private String content1;

    @Column
    private String title2;

    @Column
    private String content2;

    @Column
    private String title3;

    @Column
    private String content3;

    @Column
    private String title4;

    @Column
    private String content4;

    @Column
    private String title5;

    @Column
    private String content5;

    @Column
    private LocalDateTime registeredDate;

    @ManyToOne
    private User user;

    public void modifyIntroduction(IntroductionDto introductionDto) {

    }

    public void setUsers(User currentUser) {
        if(this.user != null) this.user.getIntroductions().remove(this);

        this.user = currentUser;
        currentUser.getIntroductions().add(this);
    }
}
