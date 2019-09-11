package com.dblab.domain;

import lombok.*;

/**
 * 사람인 API 사용
 */

@Getter
@Setter
@NoArgsConstructor
public class HireInformation {

    // 기업명, 공고명, 업직종 키워드, 직무내용에서 검색하고자 하는 검색어
    private String keywords;

    // 근무지/지역 조건(참고 : https://oapi.saramin.co.kr/guide/code-table2)
    private String loc_cd;

    // 근무지/지역 조건(참고 : https://oapi.saramin.co.kr/guide/code-table2)
    private String loc_mcd;

    // 근무지/지역 조건(참고 : https://oapi.saramin.co.kr/guide/code-table2)
    private String loc_bcd;

    /**
     * 검색 결과의 정렬 순서
     * pd: 게시일 역순(기본값)
     * pa: 게시일순
     * ud: 최근수정순
     * ua: 수정일 정순
     * da: 마감일 정순
     * dd: 마감일 역순
     * rc: 조회수 역순
     * ac: 지원자수 역순
     */
    private String sort;

    @Builder
    public HireInformation(String keywords, String loc_cd, String loc_mcd, String loc_bcd, String sort) {
        this.keywords = keywords;
        this.loc_cd = loc_cd;
        this.loc_mcd = loc_mcd;
        this.loc_bcd = loc_bcd;
        this.sort = sort;
    }
}
