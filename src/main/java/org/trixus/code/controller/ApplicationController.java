package org.trixus.code.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.trixus.code.dto.ApplicationDto;
import org.trixus.code.model.Application;
import org.trixus.code.model.vo.Status;
import org.trixus.code.service.ApplicationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    private final ApplicationService applicationService;
    private ModelMapper modelMapper;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/submit")
    public ApplicationDto submitApplication (@RequestBody ApplicationDto applicationRequest) {
        return applicationService.createApplication(applicationRequest);
    }

    @GetMapping("/submittedApplications")
    public List<ApplicationDto> getAllApplications (){
        return applicationService.getAllApplication();
    }

    @GetMapping("/application/{status}")
    public List<ApplicationDto> getAllApplicationsByStatus(@PathVariable("status") String status) {
        return applicationService.getApplicationsByStatus(status);
    }

    @PutMapping("/update/{id}/{status}")
    public ApplicationDto updateApplicationByStatus(@PathVariable("id") Long id , @PathVariable("status") String status) {
        return applicationService.updateApplicationStatus(id, Status.valueOf(status));
    }

    @GetMapping("/{id}")
    public Optional<ApplicationDto> getApplicationById (@PathVariable("id") Long id) {
        return applicationService.getApplicationById(id);
    }

    @PutMapping("/updateApplication/{id}")
    public ApplicationDto updateApplication(@RequestBody ApplicationDto application,@PathVariable("id") Long applicationId) {
        return applicationService.updateApplication(applicationId, application);
    }
    @DeleteMapping("application/{id}")
    public void deleteApplication (@PathVariable("id") Long id) {
        applicationService.deleteApplicationById(id);
    }

}
