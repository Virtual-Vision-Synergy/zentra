package org.pentagone.business.zentracore.hr.service.impl;

import jakarta.transaction.Transactional;
import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.hr.entity.Application;
import org.pentagone.business.zentracore.hr.entity.Token;
import org.pentagone.business.zentracore.hr.repository.ApplicationRepository;
import org.pentagone.business.zentracore.hr.repository.TokenRepository;
import org.pentagone.business.zentracore.hr.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final ApplicationRepository applicationRepository;
    @Value("token.expiration-days.default-value")
    private Integer expirationDays;


    public TokenServiceImpl(TokenRepository tokenRepository, ApplicationRepository applicationRepository) {
        this.tokenRepository = tokenRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Token generateTokenByApplicationId(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() ->
                new EntityNotFoundException("Application not found"));
        Token token = new Token();
        token.setValue("qcm_token_" + UUID.randomUUID() + "_" + LocalDateTime.now());
        token.setActive(true);
        token.setExpirationDate(LocalDateTime.now().plusDays(expirationDays));
        token.setApplication(application);
        return tokenRepository.save(token);
    }
}
