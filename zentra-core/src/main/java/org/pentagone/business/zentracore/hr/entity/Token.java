package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table("token")
@Data
@EqualsAndHashCode(callSuper = true)
public class Token extends BaseEntity {
    private String value;
    private LocalDateTime expirationDate;
    @OneToOne
    @JoinColumn(name = "application_id")
    private Application application;

    public boolean isValid() {
        return LocalDateTime.now().isBefore(expirationDate);
    }
}
