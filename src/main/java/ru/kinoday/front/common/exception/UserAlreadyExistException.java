package firsov.study.securitySpring.exception;

import javax.validation.constraints.NotEmpty;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(@NotEmpty String s) {
    }
}
