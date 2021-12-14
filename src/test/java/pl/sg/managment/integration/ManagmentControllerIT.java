package pl.sg.managment.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import pl.sg.managment.handler.ApplicationExceptionHandler;
import pl.sg.managment.model.Application;
import pl.sg.managment.repository.ApplicationRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ManagmentControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ApplicationRepository applicationRepository;

    @MockBean
    private ApplicationExceptionHandler applicationExceptionHandler;

    private String applicationApiUrl = "/application/";

    @Test
    void testGetApplicationById_expectApplication() {
        //given
        Application givenApp = new Application();
        givenApp.setName("name");
        givenApp.setContent("content");
        applicationRepository.saveAndFlush(givenApp);

        //when
        Application application = restTemplate
                .getForObject("http://localhost:" + port + applicationApiUrl + 1, Application.class);

        //then
        assertEquals(1, application.getId());
    }

    @Test
    void testGetApplicationById_withInvalidId_expectExceptionThrownAndHandled() {
        //given
        String url = "http://localhost:" + port + applicationApiUrl + 100;

        //when
        restTemplate.getForObject(url, Application.class);

        //then
        verify(applicationExceptionHandler, times(1)).applicationNotFoundException(any(), any());
    }

}
