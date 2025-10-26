package org.pentagone.business.zentracore.hr.service.impl;

import org.pentagone.business.zentracore.hr.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public void uploadFile(MultipartFile file, String dirName) {
        try {
            if (file.isEmpty())
                throw new IllegalArgumentException("File is empty");
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null)
                throw new IllegalArgumentException("File name is invalid");
            Path uploadPath = Path.of(System.getProperty("user.dir") + "/" + dirName);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(originalFilename);
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to upload file: " + e.getMessage());
        }
    }

}
