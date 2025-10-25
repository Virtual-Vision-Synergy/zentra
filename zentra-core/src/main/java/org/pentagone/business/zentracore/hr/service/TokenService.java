package org.pentagone.business.zentracore.hr.service;

import org.pentagone.business.zentracore.hr.entity.Token;

public interface TokenService {
    Token generateTokenByApplicationId(Long applicationId);
}
