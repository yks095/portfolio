package com.dblab.service;

import com.dblab.controller.LicenseRestController;
import com.dblab.controller.ProjectRestController;
import com.dblab.domain.License;
import com.dblab.domain.Project;
import com.dblab.domain.User;
import com.dblab.dto.LicenseDto;
import com.dblab.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class LicenseService {

    @Autowired
    LicenseRepository licenseRepository;

    public PagedResources<License> getLicenses(Pageable pageable, User currentUser) {

        Page<License> licenses = licenseRepository.findAllByUser(pageable, currentUser);
        PagedResources.PageMetadata pageMetadata = new PagedResources.PageMetadata(pageable.getPageSize(), licenses.getNumber(), licenses.getTotalElements());
        PagedResources<License> resources = new PagedResources<>(licenses.getContent(), pageMetadata);
        resources.add(linkTo(methodOn(LicenseRestController.class).licenseView(pageable)).withSelfRel());

        return resources;

    }

    public void saveLicense(LicenseDto licenseDto, User user) {
        licenseRepository.save(licenseDto.save(user));
    }

    public void modifyLicense(Long idx, LicenseDto licenseDto) {
        License modifiedLicense= licenseRepository.findByIdx(idx);
        modifiedLicense.modifyLicense(licenseDto);

        licenseRepository.save(modifiedLicense);
    }

    public void deleteLicense(Long idx) {
        licenseRepository.deleteById(idx);
    }
}
