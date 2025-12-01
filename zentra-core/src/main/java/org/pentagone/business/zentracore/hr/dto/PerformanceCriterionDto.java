package org.pentagone.business.zentracore.hr.dto;

public class PerformanceCriterionDto {
    private Long id;
    private String code;
    private String label;
    private String description;
    private Double defaultWeight;
    private String category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDefaultWeight() {
        return defaultWeight;
    }

    public void setDefaultWeight(Double defaultWeight) {
        this.defaultWeight = defaultWeight;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

