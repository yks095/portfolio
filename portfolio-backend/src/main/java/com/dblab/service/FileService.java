package com.dblab.service;

import com.dblab.config.FileConfig;
import com.dblab.config.exception.FileDownloadException;
import com.dblab.config.exception.FileUploadException;
import com.dblab.controller.UserController;
import com.dblab.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    private final Path fileLocation;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ResourceLoader resourceLoader;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String USER_DEFAULT_IMG = "default_user.png";
    private static final String PROJECT_DEFAULT_IMG = "default_project.png";

    @Autowired
    public FileService(FileConfig config) {
        this.fileLocation = Paths.get(config.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileLocation);
        }catch(Exception e) {
            throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
        }
    }


    // 파일 저장
    public String storeFile(MultipartFile file, HttpServletRequest request) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        String randomUuid = UUID.randomUUID().toString();
        fileName = randomUuid + "_" + fileName;

        // 파일명에 부적합 문자가 있는지 확인한다.
        if(fileName.contains(".."))
            throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);

        Path targetLocation = this.fileLocation.resolve(fileName);

        try {

            String requestUrl = request.getRequestURI() + "/";
            System.out.println(requestUrl);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(requestUrl)
                    .path(fileName)
                    .toUriString();

            return fileDownloadUri;

        }catch(Exception e) {
            throw new FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
        }
    }

    // 파일 다운로드
    public ResponseEntity<Resource> loadFileAsResource(String fileName, HttpServletRequest request) {
        try {
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {
                String contentType = null;
                try {
                    contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                } catch (IOException ex) {
                    logger.info("Could not determine file type.");
                }

                // Fallback to the default content type if type could not be determined
                if(contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            }else {
                throw new FileDownloadException(fileName + " 파일을 찾을 수 없습니다.");
            }
        }catch(MalformedURLException e) {
            throw new FileDownloadException(fileName + " 파일을 찾을 수 없습니다.", e);
        }
    }

    // 저장한 이미지가 없을 시 디폴트 이미지 자동 저장
    public String setDefaultImage(HttpServletRequest request) {

        String defaultUrl = request.getRequestURI() + "/";
        String fileDownloadUri = "";

        if (defaultUrl.contains("user")) {
            fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(defaultUrl)
                    .path(USER_DEFAULT_IMG)
                    .toUriString();

        }
        else if (defaultUrl.contains("projects")) {
            fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(defaultUrl)
                    .path(PROJECT_DEFAULT_IMG)
                    .toUriString();
        }

            return fileDownloadUri;
        }

    }
