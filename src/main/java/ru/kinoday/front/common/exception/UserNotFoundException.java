package firsov.study.securitySpring.exception;

import javax.validation.constraints.NotEmpty;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(@NotEmpty String s) {
    }
}
