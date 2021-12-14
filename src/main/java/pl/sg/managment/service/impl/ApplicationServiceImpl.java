package pl.sg.managment.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.sg.managment.dto.ApplicationDTO;
import pl.sg.managment.exception.ApplicationNotFoundException;
import pl.sg.managment.exception.NoReasonException;
import pl.sg.managment.exception.WrongStateTransitionException;
import pl.sg.managment.model.Application;
import pl.sg.managment.model.State;
import pl.sg.managment.model.StateChange;
import pl.sg.managment.repository.ApplicationRepository;
import pl.sg.managment.service.ApplicationService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createApplication(ApplicationDTO applicationDTO) {
        Application application = modelMapper.map(applicationDTO, Application.class);
        changeStateAndSaveApplication(application, State.CREATED, Optional.empty());
    }

    /**
     * Treść wniosku może się zmienić podczas zmiany stanu na VERIFIED czy gdy jest już w stanie VERIFIED?
     * @param paramId
     */
    @Override
    public void verifyApplication(String paramId, String content) {
        Application application = findApplicationById(paramId);
        if (!application.getState().equals(State.CREATED)) {
            throw new WrongStateTransitionException();
        }
        if (!content.isBlank()) {
            application.setContent(content);
        }
        changeStateAndSaveApplication(application, State.VERIFIED, Optional.empty());
    }

    @Override
    public void acceptApplication(String paramId) {
        Application application = findApplicationById(paramId);
        if (!application.getState().equals(State.VERIFIED)) {
            throw new WrongStateTransitionException();
        }
        changeStateAndSaveApplication(application, State.ACCEPTED, Optional.empty());
    }

    @Override
    public Application publishApplication(String paramId) {
        Application application = findApplicationById(paramId);
        if (!application.getState().equals(State.ACCEPTED)) {
            throw new WrongStateTransitionException();
        }
        application.setUniqueNumber(application.getId()); //TODO
        return changeStateAndSaveApplication(application, State.PUBLISHED, Optional.empty());
    }

    @Override
    public void rejectApplication(String paramId, String reason) {
        checkReason(reason);
        Application application = findApplicationById(paramId);
        if (!application.getState().equals(State.VERIFIED) || !application.getState().equals(State.ACCEPTED)) {
            throw new WrongStateTransitionException();
        }
        changeStateAndSaveApplication(application, State.REJECTED, Optional.of(reason));
    }

    @Override
    public void deleteApplication(String paramId, String reason) {
        checkReason(reason);
        Application application = findApplicationById(paramId);
        if (!application.getState().equals(State.CREATED)) {
            throw new WrongStateTransitionException();
        }
        changeStateAndSaveApplication(application, State.DELETED, Optional.of(reason));
    }

    @Override
    public Page<Application> findListOfApplications(Optional<String> filter, Optional<String> state, int page, int size) {
        return applicationRepository.findByNameAndState(
                filter.orElse(""),
                State.valueOf(state.orElse("")),
                PageRequest.of(page, size));
    }

    private Integer convertedId(String paramId) {
        return Integer.valueOf(paramId);
    }

    public Application findApplicationById(String paramId) {
        return applicationRepository.findById(convertedId(paramId)).orElseThrow(ApplicationNotFoundException::new);
    }

    private void checkReason(String reason) {
        if (reason.isBlank()) {
            throw new NoReasonException();
        }
    }

    private Application changeStateAndSaveApplication(Application application, State newState, Optional<String> reason) {
        List<StateChange> stateChangeList = application.getStateChange();
        stateChangeList.add(new StateChange(newState, reason.orElse(null)));
        application.setStateChange(stateChangeList);
        application.setState(newState);
        return applicationRepository.save(application);
    }

}
