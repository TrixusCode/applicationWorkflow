package org.trixus.code.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.trixus.code.dto.ApplicationDto;
import org.trixus.code.model.vo.Status;
import org.trixus.code.service.ApplicationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    private final ApplicationService applicationService;
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/submit")
    public ResponseEntity<ApplicationDto> submitApplication (@RequestBody ApplicationDto applicationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.createApplication(applicationRequest));
    }

    @GetMapping("/submittedApplications")
    public ResponseEntity<List<ApplicationDto>> getAllApplications (){
        return ResponseEntity.status(HttpStatus.OK).body(applicationService.getAllApplication());
    }

    @GetMapping("/application/{status}")
    public ResponseEntity<List<ApplicationDto>> getAllApplicationsByStatus (@PathVariable("status") String status) {
        return ResponseEntity.status(HttpStatus.OK).body(applicationService.getApplicationsByStatus(status));
    }

    @PutMapping("/update/{id}/{status}")
    public ResponseEntity<ApplicationDto> updateApplicationByStatus (@PathVariable("id") Long id , @PathVariable("status") String status) {
        return ResponseEntity.status(HttpStatus.OK).body(applicationService.updateApplicationStatus(id, Status.valueOf(status)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ApplicationDto>> getApplicationById (@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(applicationService.getApplicationById(id));
    }

    @PutMapping("/updateApplication/{id}")
    public ResponseEntity<ApplicationDto> updateApplication(@RequestBody ApplicationDto application,@PathVariable("id") Long applicationId) {
        return ResponseEntity.status(HttpStatus.OK).body(applicationService.updateApplication(applicationId, application));
    }
    @DeleteMapping("application/{id}")
    public void deleteApplication (@PathVariable("id") Long id) {
        applicationService.deleteApplicationById(id);
    }

}
