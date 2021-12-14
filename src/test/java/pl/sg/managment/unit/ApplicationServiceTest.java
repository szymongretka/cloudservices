package pl.sg.managment.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.sg.managment.dto.ApplicationDTO;
import pl.sg.managment.model.Application;
import pl.sg.managment.model.State;
import pl.sg.managment.repository.ApplicationRepository;
import pl.sg.managment.service.ApplicationService;
import pl.sg.managment.service.impl.ApplicationServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ModelMapper modelMapper;

    private ApplicationService applicationService;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationServiceImpl(applicationRepository, modelMapper);
    }

    @Test
    void testcreateApplication_expectApplicationCreated() {
        //given
        String givenName = "name";
        Application givenApplication = new Application();
        givenApplication.setName(givenName);
        ApplicationDTO givenApplicationDTO = new ApplicationDTO();
        givenApplicationDTO.setName(givenName);

        when(modelMapper.map(givenApplicationDTO, Application.class)).thenReturn(givenApplication);
        when(applicationRepository.save(givenApplication)).thenReturn(givenApplication);

        //when
        applicationService.createApplication(givenApplicationDTO);

        //then
        assertEquals(State.CREATED, givenApplication.getState());
    }

}
