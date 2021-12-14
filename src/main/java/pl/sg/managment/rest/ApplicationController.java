package pl.sg.managment.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;
import pl.sg.managment.dto.ApplicationDTO;
import pl.sg.managment.model.Application;
import pl.sg.managment.service.ApplicationService;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/application/")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping(value = "all")
    public CollectionModel<Application> findApplications(@RequestParam Optional<String> name,
                                                         @RequestParam Optional<String> state,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {

        Page<Application> applicationPage = applicationService.findListOfApplications(name, state, page, size);

        for (final Application application : applicationPage.getContent()) {
            Link selfLink = linkTo(methodOn(ApplicationController.class)
                    .getApplication(String.valueOf(application.getId()))).withSelfRel();
            application.add(selfLink);
        }

        Link link = linkTo(methodOn(ApplicationController.class)
                .findApplications(name, state, page, size)).withSelfRel();
        CollectionModel<Application> result = CollectionModel.of(applicationPage.getContent(), link);

        return result;
    }

    @PostMapping(path = "create")
    public String createApplication(@RequestBody ApplicationDTO applicationDTO) {
        applicationService.createApplication(applicationDTO);
        return "Created!";
    }

    @PutMapping("delete/{id}")
    public String deleteApplication(@PathVariable String id, @RequestBody ObjectNode reasonNode) {
        String reason = reasonNode.get("reason").asText("");
        applicationService.deleteApplication(id, reason);
        return "Deleted application with id: " + id;
    }

    @PutMapping("verify/{id}")
    public String verifyApplication(@PathVariable String id, @RequestBody ObjectNode contentNode) {
        String content = contentNode.get("content").asText("");
        applicationService.verifyApplication(id, content);
        return "Verified application with id: " + id;
    }

    @PutMapping("reject/{id}")
    public String rejectApplication(@PathVariable String id, @RequestBody ObjectNode reasonNode) {
        String reason = reasonNode.get("reason").asText("");
        applicationService.rejectApplication(id, reason);
        return "Rejected application with id: " + id;
    }

    @PutMapping("accept/{id}")
    public String acceptApplication(@PathVariable String id) {
        applicationService.acceptApplication(id);
        return "Accepted application with id: " + id;
    }

    @PutMapping("publish/{id}")
    public Application publishApplication(@PathVariable String id) {
        return applicationService.publishApplication(id);
    }

    @GetMapping("{id}")
    public Application getApplication(@PathVariable String id) {
        return applicationService.findApplicationById(id);
    }

}
