package com.dblab.domain;

import com.dblab.dto.LicenseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Entity
@Table
@Getter
@Setter
public class License implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String name;

    @Column
    private String period;

    @Column
    private String institution;

    @ManyToOne
    private User user;

    @Builder
    public License(String name, String period, String institution, User user) {
        this.name = name;
        this.period = period;
        this.institution = institution;
        this.user = user;
    }

    public void setUsers(User currentUser) {
        if(this.user != null) this.user.getLicenses().remove(this);

        this.user = currentUser;
        currentUser.getLicenses().add(this);
    }

    public void modifyLicense(LicenseDto licenseDto) {
        this.name = licenseDto.getName();
        this.period = licenseDto.getPeriod();
        this.institution = licenseDto.getInstitution();
    }
}
