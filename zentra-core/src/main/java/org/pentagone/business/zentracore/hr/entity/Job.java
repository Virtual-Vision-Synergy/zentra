package org.pentagone.business.zentracore.hr.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pentagone.business.zentracore.common.entity.BaseEntity;

import java.util.List;


@Entity
@Table(name = "job")
@Data
@EqualsAndHashCode(callSuper = true)
public class Job extends BaseEntity {

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "required_degree", columnDefinition = "TEXT")
    private String requiredDegree;

    @Column(name = "required_skills", columnDefinition = "TEXT")
    private String requiredSkills;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publication> publications;
}
