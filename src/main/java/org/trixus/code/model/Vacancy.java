package org.trixus.code.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;
    private LocalDateTime vacancyClosingDate;
    private String vacancyTitle;
    private LocalDateTime vacancyScreeningDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getVacancyClosingDate() {
        return vacancyClosingDate;
    }

    public void setVacancyClosingDate(LocalDateTime vacancyClosingDate) {
        this.vacancyClosingDate = vacancyClosingDate;
    }

    public String getVacancyTitle() {
        return vacancyTitle;
    }

    public void setVacancyTitle(String vacancyTitle) {
        this.vacancyTitle = vacancyTitle;
    }

    public LocalDateTime getVacancyScreeningDate() {
        return vacancyScreeningDate;
    }

    public void setVacancyScreeningDate(LocalDateTime vacancyScreeningDate) {
        this.vacancyScreeningDate = vacancyScreeningDate;
    }
}
