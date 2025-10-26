package org.pentagone.business.zentracore.hr.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void uploadFile(MultipartFile file, String dirName);
}
