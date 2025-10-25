package org.pentagone.business.zentracore.hr.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenDto {
    String value;
    LocalDateTime expirationDate;
}
