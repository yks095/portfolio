package com.dblab.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileUpload {

    private String fileName;

    private long fileSize;

    private String fileContentType;

    private String attachmentUrl;

    public FileUpload(String fileName, long fileSize, String fileContentType, String attachmentUrl) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileContentType = fileContentType;
        this.attachmentUrl = attachmentUrl;
    }

}
