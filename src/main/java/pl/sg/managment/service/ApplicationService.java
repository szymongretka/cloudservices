package pl.sg.managment.service;

import org.springframework.data.domain.Page;
import pl.sg.managment.dto.ApplicationDTO;
import pl.sg.managment.model.Application;

import java.util.Optional;

public interface ApplicationService {
    void createApplication(ApplicationDTO applicationDTO);
    void verifyApplication(String id, String content);
    void acceptApplication(String id);
    Application publishApplication(String id);
    void rejectApplication(String id, String reason);
    void deleteApplication(String id, String reason);
    Page<Application> findListOfApplications(Optional<String> name, Optional<String> state, int page, int size);
    Application findApplicationById(String id);
}
