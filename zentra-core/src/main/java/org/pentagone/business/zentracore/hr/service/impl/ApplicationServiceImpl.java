package org.pentagone.business.zentracore.hr.service.impl;

import jakarta.transaction.Transactional;
import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.hr.dto.ApplicationDto;
import org.pentagone.business.zentracore.hr.dto.AssignQcmDto;
import org.pentagone.business.zentracore.hr.dto.SetDocumentScoreDto;
import org.pentagone.business.zentracore.hr.entity.Application;
import org.pentagone.business.zentracore.hr.entity.Candidate;
import org.pentagone.business.zentracore.hr.entity.Qcm;
import org.pentagone.business.zentracore.hr.entity.Token;
import org.pentagone.business.zentracore.hr.mapper.ApplicationMapper;
import org.pentagone.business.zentracore.hr.repository.ApplicationRepository;
import org.pentagone.business.zentracore.hr.repository.QcmRepository;
import org.pentagone.business.zentracore.hr.service.ApplicationService;
import org.pentagone.business.zentracore.hr.service.FileService;
import org.pentagone.business.zentracore.hr.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    private final FileService fileService;
    private final MailServiceImpl mailService;
    private final TokenService tokenService;
    private final ApplicationRepository applicationRepository;
    private final QcmRepository qcmRepository;
    private final ApplicationMapper applicationMapper;
    @Value("${upload-dir.candidates.cvs}")
    private String cvsUploadDir;
    @Value("${upload-dir.candidates.motivation-letters}")
    private String motivationLettersUploadDir;

    public ApplicationServiceImpl(FileService fileService, MailServiceImpl mailService, TokenService tokenService, ApplicationRepository applicationRepository, QcmRepository qcmRepository, ApplicationMapper applicationMapper) {
        this.fileService = fileService;
        this.mailService = mailService;
        this.tokenService = tokenService;
        this.applicationRepository = applicationRepository;
        this.qcmRepository = qcmRepository;
        this.applicationMapper = applicationMapper;
    }

    void verifyCandidate(Candidate candidate) {
        if (candidate.getFirstName() == null || candidate.getFirstName().isBlank())
            throw new IllegalArgumentException("Le prénom du candidat est obligatoire");
        if (candidate.getLastName() == null || candidate.getLastName().isBlank())
            throw new IllegalArgumentException("Le nom du candidat est obligatoire");
        if (candidate.getEmail() == null || candidate.getEmail().isBlank())
            throw new IllegalArgumentException("L'email du candidat est obligatoire");
        if (candidate.getPhone() == null || candidate.getPhone().isBlank())
            throw new IllegalArgumentException("Le téléphone du candidat est obligatoire");
        if (candidate.getAddress() == null || candidate.getAddress().isBlank())
            throw new IllegalArgumentException("L'adresse du candidat est obligatoire");
        if (candidate.getCity() == null || candidate.getCity().isBlank())
            throw new IllegalArgumentException("La ville du candidat est obligatoire");
        if (candidate.getCountry() == null || candidate.getCountry().isBlank())
            throw new IllegalArgumentException("Le pays du candidat est obligatoire");
        if (candidate.getEducationLevel() == null || candidate.getEducationLevel().isBlank())
            throw new IllegalArgumentException("Le niveau d'éducation du candidat est obligatoire");
        if (candidate.getLastDegree() == null || candidate.getLastDegree().isBlank())
            throw new IllegalArgumentException("Le dernier diplôme du candidat est obligatoire");
        if (candidate.getYearsExperience() == null || candidate.getYearsExperience() < 0)
            throw new IllegalArgumentException("Les années d'expérience du candidat sont obligatoires");
        if (candidate.getSkills() == null || candidate.getSkills().isBlank())
            throw new IllegalArgumentException("Les compétences du candidat sont obligatoires");
    }

    private void verifyApplication(Application application) {
        if (application.getCandidate() == null)
            throw new IllegalArgumentException("Le candidat est obligatoire pour une candidature");
        if (application.getPublication() == null)
            throw new IllegalArgumentException("La publication est obligatoire pour une candidature");
        verifyCandidate(application.getCandidate());
    }

    @Override
    public ApplicationDto createApplication(ApplicationDto applicationDto, MultipartFile cv, MultipartFile motivationLetter) {
        Application application = applicationMapper.toEntity(applicationDto);
        if (application.getId() != null)
            throw new IllegalArgumentException("Une nouvelle candidature ne peut pas avoir d'ID");
        verifyApplication(application);
        fileService.uploadFile(cv, cvsUploadDir);
        fileService.uploadFile(motivationLetter, motivationLettersUploadDir);
        application.getCandidate().setCvFile(cvsUploadDir + "/" + cv.getOriginalFilename());
        application.getCandidate().setMotivationalLetterFile(motivationLettersUploadDir + "/" + motivationLetter.getOriginalFilename());
        application.setAppliedAt(LocalDateTime.now());
        receivedApplication(application);
        return applicationMapper.toDto(applicationRepository.save(application));
    }

    @Override
    public ApplicationDto getApplicationById(Long id) {
        return applicationMapper.toDto(applicationRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Application not found with id: " + id)));
    }

    @Override
    public List<ApplicationDto> getApplications() {
        return applicationRepository.findAll().stream().map(applicationMapper::toDto).toList();
    }

    @Override
    public ApplicationDto updateApplication(ApplicationDto applicationDto) {
        Application application = applicationMapper.toEntity(applicationDto);
        if (application.getId() == null || !applicationRepository.existsById(application.getId()))
            throw new EntityNotFoundException("Application not found with id: " + application.getId());
        verifyApplication(application);
        return applicationMapper.toDto(applicationRepository.save(application));
    }

    @Override
    public Void deleteApplicationById(Long id) {
        Application application = applicationRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Application not found with id: " + id));
        applicationRepository.delete(application);
        return null;
    }

    @Override
    public SetDocumentScoreDto setDocumentScore(SetDocumentScoreDto setDocumentScoreDto) {
        if (setDocumentScoreDto.getApplicationId() == null)
            throw new IllegalArgumentException("L'ID de la candidature est obligatoire");
        if (setDocumentScoreDto.getDocumentScore() == null ||
                setDocumentScoreDto.getDocumentScore() < 0 ||
                setDocumentScoreDto.getDocumentScore() > 20)
            throw new IllegalArgumentException("Le score du document doit être compris entre 0 et 20");
        Application application = applicationRepository.findById(setDocumentScoreDto.getApplicationId()).orElseThrow(() ->
                new EntityNotFoundException("Application not found with id: " + setDocumentScoreDto.getApplicationId()));
        application.setDocumentScore(setDocumentScoreDto.getDocumentScore());
        applicationRepository.save(application);
        if (application.getDocumentScore() < 10) rejectApplication(application);
        else acceptDocumentApplication(application);
        return setDocumentScoreDto;
    }

    @Override
    public AssignQcmDto assignQcm(AssignQcmDto assignQcmDto) {
        if (assignQcmDto.getApplicationId() == null)
            throw new IllegalArgumentException("L'ID de la candidature est obligatoire");
        if (assignQcmDto.getQcmId() == null)
            throw new IllegalArgumentException("L'ID du QCM est obligatoire");
        Application application = applicationRepository.findById(assignQcmDto.getApplicationId()).orElseThrow(() ->
                new EntityNotFoundException("Application not found with id: " + assignQcmDto.getApplicationId()));
        Qcm qcm = qcmRepository.findById(assignQcmDto.getQcmId()).orElseThrow(() ->
                new EntityNotFoundException("QCM not found with id: " + assignQcmDto.getQcmId()));
        application.setQcm(qcm);
        Token token = tokenService.generateTokenByApplicationId(assignQcmDto.getApplicationId());
        application.setStatus("test_scheduled");
        mailService.sendEmail(application.getCandidate().getEmail(),
                "Candidature - Test programmé",
                "Bonjour " + application.getCandidate().getFirstName() + ",\n\n" +
                        "Votre test de recrutement a été programmé. Veuillez passer le test sur notre plateforme en utilisant le lien suivant :\n" +
                        "http://localhost:5173/ en utilisant le token suivant :  " + token.getValue() + "\n\n" +
                        "Passer ce test avant la date : " + token.getExpirationDate() + "\n\n" +
                        "Cordialement,\nL'équipe RH");
        applicationRepository.save(application);
        return assignQcmDto;
    }

    public void receivedApplication(Application application) {
        application.setStatus("received");
        mailService.sendEmail(application.getCandidate().getEmail(),
                "Candidature reçue",
                "Bonjour " + application.getCandidate().getFirstName() + ",\n\n" +
                        "Nous avons bien reçu votre candidature pour le poste publié. Nous l'examinerons attentivement et vous contacterons prochainement.\n\n" +
                        "Cordialement,\nL'équipe RH");
        applicationRepository.save(application);
    }

    public void rejectApplication(Application application) {
        application.setStatus("rejected");
        mailService.sendEmail(application.getCandidate().getEmail(),
                "Candidature rejetée",
                "Bonjour " + application.getCandidate().getFirstName() + ",\n\n" +
                        "Nous regrettons de vous informer que votre candidature pour le poste publié n'a pas été retenue. Nous vous remercions pour l'intérêt que vous avez porté à notre entreprise et vous souhaitons le meilleur pour vos futures démarches.\n\n" +
                        "Cordialement,\nL'équipe RH");
        applicationRepository.save(application);
    }

    public void acceptDocumentApplication(Application application) {
        application.setStatus("document_accepted");
        mailService.sendEmail(application.getCandidate().getEmail(),
                "Candidature - Documents acceptés",
                "Bonjour " + application.getCandidate().getFirstName() + ",\n\n" +
                        "Félicitations ! Vos documents ont été acceptés pour le poste publié. Nous vous contacterons prochainement pour les prochaines étapes du processus de recrutement.\n\n" +
                        "Cordialement,\nL'équipe RH");
        applicationRepository.save(application);
    }

}
