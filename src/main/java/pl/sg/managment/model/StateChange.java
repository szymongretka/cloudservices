package pl.sg.managment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class StateChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private State state;

    private String reason;

    public StateChange(State state, String reason) {
        this.reason = reason;
        this.state = state;
    }

}
