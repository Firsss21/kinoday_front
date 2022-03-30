package firsov.study.securitySpring.model;

import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
public class PasswordResetToken {

    private static final int EXPIRATION_TIME = 60 * 30 * 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
    }

    public PasswordResetToken() {

    }
}
