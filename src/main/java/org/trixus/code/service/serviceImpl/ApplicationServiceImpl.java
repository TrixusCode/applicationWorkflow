package org.trixus.code.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trixus.code.dto.ApplicationDto;
import org.trixus.code.model.Application;
import org.trixus.code.model.vo.Status;
import org.trixus.code.repository.ApplicationRepo;
import org.trixus.code.service.ApplicationService;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepo applicationRepo;
    private ModelMapper modelMapper;

    public ApplicationServiceImpl(ApplicationRepo applicationRepo){
        this.applicationRepo = applicationRepo;
    }
    @Transactional
    @Override
    public ApplicationDto createApplication(ApplicationDto applicationRequest) {

        Application applicationRequestEntity = modelMapper.map(applicationRequest, Application.class);

        Application newApplication = new Application();

        newApplication.setFirstName(applicationRequestEntity.getFirstName());
        newApplication.setLastName(applicationRequestEntity.getLastName());
        newApplication.setStatus(Status.Application_Submitted);
        newApplication.setDateOfBirth(applicationRequestEntity.getDateOfBirth());
        newApplication.setMiddleName(applicationRequestEntity.getMiddleName());
        newApplication.setApplicationNumber(generateApplicationNumber());
        newApplication.setCreatedDate(LocalDateTime.now());
        newApplication.setSubmittedDate(LocalDateTime.now());
        newApplication.setVacancyId(applicationRequestEntity.getVacancyId());


        applicationRepo.save(newApplication);


        return modelMapper.map(newApplication, ApplicationDto.class);
    }


    public static String generateApplicationNumber() {

        String letters = generateRandomLetters(3);

        String digits = generateRandomDigits(4);

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        return letters + digits + date;
    }

    private static String generateRandomLetters(int length) {
        StringBuilder letters = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char randomChar = (char) ('A' + random.nextInt(26));
            letters.append(randomChar);
        }
        return letters.toString();
    }

    private static String generateRandomDigits(int length) {
        StringBuilder digits = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomDigit = random.nextInt(10);
            digits.append(randomDigit);
        }
        return digits.toString();
    }

    @Transactional
    @Override
    public ApplicationDto updateApplication(Long applicationId, ApplicationDto applicationUpdate) {
        Application applicationUpdateEntity = modelMapper.map(applicationUpdate, Application.class);
        if (applicationId == null || applicationUpdate == null) {
            throw new RuntimeException("Application Id and/or Application cannot be null");
        }

        Optional<Application> applicationOptional = applicationRepo.findById(applicationId);

        if (applicationOptional.isEmpty()) {
            throw new RuntimeException("Application to be updated not found");
        }

        Application application = applicationOptional.get();


        application.setStatus(applicationUpdateEntity.getStatus());
        application.setFirstName(applicationUpdateEntity.getFirstName());
        application.setDateOfBirth(applicationUpdateEntity.getDateOfBirth());
        application.setMiddleName(applicationUpdateEntity.getMiddleName());
        application.setLastName(applicationUpdateEntity.getLastName());
        applicationRepo.save(application);
        return modelMapper.map(application, ApplicationDto.class);
    }


    @Override
    public List<ApplicationDto> getAllApplication() {
        List<Application> applicationEntity= applicationRepo.findAll();
        return Collections.singletonList(modelMapper.map(applicationEntity, ApplicationDto.class));
    }

    @Override
    public void deleteApplicationById(Long applicationId) {

       Optional<Application> fetchedApplication = applicationRepo.findById(applicationId);

        if (fetchedApplication.isEmpty()){
            throw new RuntimeException("Application Not Found");

        }

        applicationRepo.delete(fetchedApplication.get());
    }

    @Override
    public ApplicationDto updateApplicationStatus (Long applicationId, Status status) {

        Optional<Application> applicationOptional = applicationRepo.findById(applicationId);

        //check if the application exist
        if (applicationOptional.isEmpty()) {
            throw new RuntimeException("No Application Found With Id:" + applicationId);
        }
        Application application = applicationOptional.get();
        application.setStatus(status);
        applicationRepo.save(application);
        return modelMapper.map(application, ApplicationDto.class);
    }

    @Override
    public Optional<ApplicationDto> getApplicationById(Long applicationId) {
        Optional<Application> application = applicationRepo.findById(applicationId);
        Application foundApplication = application.get();
        return Optional.ofNullable(modelMapper.map(foundApplication, ApplicationDto.class));
    }

    @Override
    public List<ApplicationDto> getApplicationsByStatus(String status) {
        List<Application> applications = applicationRepo.findByStatus(status);
        return Collections.singletonList(modelMapper.map(applications, ApplicationDto.class));
    }
}
