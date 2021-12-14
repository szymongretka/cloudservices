package pl.sg.managment.model;


import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Application extends RepresentationModel<Application> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int uniqueNumber;

    private String name;

    private String content;

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "application_id")
    //@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<StateChange> stateChange = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private State state;

}
