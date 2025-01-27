package org.trixus.code.cron;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.trixus.code.dto.ApplicationDto;
import org.trixus.code.model.Application;
import org.trixus.code.model.Vacancy;
import org.trixus.code.model.vo.Status;
import org.trixus.code.service.ApplicationService;
import org.trixus.code.service.VacancyService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationStatusUpdater {
    @Value("${scheduler.cron.update-status}")
    private String cronExpression;
    private final ApplicationService applicationService;
    private final VacancyService vacancyService;
    private ModelMapper modelMapper;

    public ApplicationStatusUpdater(ApplicationService applicationService, VacancyService vacancyService) {
        this.applicationService = applicationService;
        this.vacancyService = vacancyService;
    }

    @Scheduled(cron = "${scheduler.cron.update-status}") // Runs every day at midnight
    public void updateApplicationStatuses() {


        LocalDateTime currentDate = LocalDateTime.now();


        List<ApplicationDto> applications = applicationService.getApplicationsByStatus("Application_Submitted");
        List<Application> applicationList = Collections.singletonList(modelMapper.map(applications, Application.class));


        for (Application application : applicationList) {
            Long vacancyId = application.getVacancyId();


            Optional<Vacancy> vacancy = vacancyService.getVacancyById(vacancyId);
            if (vacancy.isEmpty()){
                throw new RuntimeException ("vacancy not found with Id:" + vacancyId);
            }
            LocalDateTime closingDate = vacancy.get().getVacancyClosingDate();

            if (closingDate.isBefore(currentDate)) {
                applicationService.updateApplicationStatus(application.getId(), Status.Pending_Screening);
            }
        }
    }
}

