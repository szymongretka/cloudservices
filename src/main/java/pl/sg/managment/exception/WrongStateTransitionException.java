package pl.sg.managment.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WrongStateTransitionException extends RuntimeException {
    private String requestedNewState;

    public WrongStateTransitionException(String message, String requestedNewState) {
        super(message);
        this.requestedNewState = requestedNewState;
    }
}
