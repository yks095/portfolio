package com.dblab.dto;

import com.dblab.domain.License;
import com.dblab.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class LicenseDto {

    @NotBlank(message = "필수 항목입니다.")
    private String name;

    @NotBlank(message = "필수 항목입니다.")
    private String period;

    @NotBlank(message = "필수 항목입니다.")
    private String institution;

    private User user;

    public License save(User user) {
        License license = new License();

        license.setName(this.name);
        license.setPeriod(this.period);
        license.setInstitution(this.institution);
        license.setUsers(user);

        return license;
    }
}
