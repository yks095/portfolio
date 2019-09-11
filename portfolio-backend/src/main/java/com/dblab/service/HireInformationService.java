package com.dblab.service;

import com.dblab.domain.HireInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Service
public class HireInformationService {

    private final String url = "https://oapi.saramin.co.kr/job-search";
    private final String accessKey = "서비스키";

    //사람인 api에 GET요청
    public Object getHireInfo(HireInformation hireInformation) {

        //변수에 null이 있을 경우 빈 문자열("")로 변환
        nullCheck(hireInformation);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url)
                                                            .queryParam("access-key", accessKey)
                                                            .queryParam("keywords", hireInformation.getKeywords())
                                                            .queryParam("loc_cd", hireInformation.getLoc_cd())
                                                            .queryParam("loc_mcd", hireInformation.getLoc_mcd())
                                                            .queryParam("loc_bcd", hireInformation.getLoc_bcd())
                                                            .queryParam("sort", hireInformation.getSort())
                                                            .build(false); //인코딩 = false

        Object response = restTemplate.exchange(uriComponents.toUriString(), HttpMethod.GET, new HttpEntity<>(httpHeaders), String.class);
        return response;
    }

    //변수에 null이 있을 경우 빈 문자열("")로 변환
    public void nullCheck(HireInformation hireInformation){
        if (hireInformation.getKeywords() == null) hireInformation.setKeywords("");
        if (hireInformation.getLoc_cd() == null) hireInformation.setLoc_cd("");
        if (hireInformation.getLoc_bcd() == null) hireInformation.setLoc_bcd("");
        if (hireInformation.getLoc_mcd() == null) hireInformation.setLoc_mcd("");
        if (hireInformation.getSort() == null) hireInformation.setSort("pd");
    }
}
