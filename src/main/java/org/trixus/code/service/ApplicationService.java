package org.trixus.code.service;

import org.trixus.code.dto.ApplicationDto;
import org.trixus.code.model.Application;
import org.trixus.code.model.vo.Status;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    public ApplicationDto createApplication (ApplicationDto applicationRequest);

    public ApplicationDto updateApplication (Long applicationId, ApplicationDto applicationUpdate);

    public List<ApplicationDto> getAllApplication ();

    public void deleteApplicationById (Long applicationId);

    public ApplicationDto updateApplicationStatus (Long applicationId, Status status);

    public Optional<ApplicationDto> getApplicationById (Long applicationId);

    public List <ApplicationDto> getApplicationsByStatus(String status);
}
