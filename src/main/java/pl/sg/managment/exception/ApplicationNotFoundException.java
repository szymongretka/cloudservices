package pl.sg.managment.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ApplicationNotFoundException extends RuntimeException {
    private int id;

    public ApplicationNotFoundException(String message, int id) {
        super(message);
        this.id = id;
    }

}
