package org.trixus.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trixus.code.model.Application;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepo extends JpaRepository <Application, Long> {

    List<Application> findByStatus (String status);
    Optional<Application> findById (Long id);

    @Override
    void delete(Application entity);

}
