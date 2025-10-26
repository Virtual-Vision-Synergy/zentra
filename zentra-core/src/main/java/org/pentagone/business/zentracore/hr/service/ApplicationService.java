package org.pentagone.business.zentracore.hr.service;

import org.pentagone.business.zentracore.hr.dto.ApplicationDto;
import org.pentagone.business.zentracore.hr.dto.AssignQcmDto;
import org.pentagone.business.zentracore.hr.dto.SetDocumentScoreDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApplicationService {
    ApplicationDto createApplication(ApplicationDto applicationDto, MultipartFile cv, MultipartFile motivationLetter);
    ApplicationDto getApplicationById(Long id);
    List<ApplicationDto> getApplications();
    ApplicationDto updateApplication(ApplicationDto applicationDto);
    Void deleteApplicationById(Long id);

    SetDocumentScoreDto setDocumentScore(SetDocumentScoreDto setDocumentScoreDto);

    AssignQcmDto assignQcm(AssignQcmDto assignQcmDto);
}
