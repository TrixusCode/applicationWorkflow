package org.trixus.code.service;

import org.trixus.code.model.Vacancy;

import java.util.Optional;

public interface VacancyService {
    Optional<Vacancy> getVacancyById(Long vacancyId);
}
