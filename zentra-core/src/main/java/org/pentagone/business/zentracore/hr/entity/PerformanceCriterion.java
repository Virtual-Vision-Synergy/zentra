package org.pentagone.business.zentracore.hr.entity;

    import jakarta.persistence.*;

    @Entity
    @Table(name = "performance_criterion")
    public class PerformanceCriterion {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false, unique = true)
        private Long id;

        @Column(nullable = false)
        private String code;

        @Column(nullable = false)
        private String label;

        @Column(length = 1000)
        private String description;

        @Column(nullable = false)
        private Double defaultWeight;

        private String category;

        public PerformanceCriterion() {
        }

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