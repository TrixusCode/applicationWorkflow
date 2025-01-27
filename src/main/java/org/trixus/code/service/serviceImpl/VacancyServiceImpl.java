package org.trixus.code.service.serviceImpl;

import org.trixus.code.model.Vacancy;
import org.trixus.code.repository.VacancyRepo;
import org.trixus.code.service.VacancyService;

import java.util.Optional;

public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepo vacancyRepo;

    public VacancyServiceImpl(VacancyRepo vacancyRepo) {
        this.vacancyRepo = vacancyRepo;
    }
    @Override
    public Optional<Vacancy> getVacancyById(Long vacancyId) {
        return Optional.ofNullable(vacancyRepo.findById(vacancyId));
    }
}
