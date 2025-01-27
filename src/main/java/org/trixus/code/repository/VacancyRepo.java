package org.trixus.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trixus.code.model.Vacancy;

public interface VacancyRepo extends JpaRepository<Long, Vacancy> {
    Vacancy findById(Long vacancyId);
}
