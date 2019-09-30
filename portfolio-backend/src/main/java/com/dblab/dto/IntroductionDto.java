package com.dblab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class IntroductionDto {

    @NotBlank(message = "필수 항목입니다.")
    private String introductionTitle;

    private String title1;

    private String content1;

    private String title2;

    private String content2;

    private String title3;

    private String content3;

    private String title4;

    private String content4;

    private String title5;

    private String content5;

}
