package bezbednosttim6.dto;

import bezbednosttim6.model.User;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterRequestDTO {

    private Long id;
    @Length(max = 100)
    private String name;
    @Length(max = 100)
    private String surname;
    @Length(max = 18)
    private String telephoneNumber;
    @Length(max = 100)
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Bad email format")
    private String email;
    private String password;
    private String validationType;

    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(Long id, String name, String surname, String telephoneNumber, String email, String password) {
        this.id                 = id;
        this.name               = name;
        this.surname            = surname;
        this.telephoneNumber    = telephoneNumber;
        this.email              = email;
        this.password           = password;
    }

    public RegisterRequestDTO(User user) {
        this.id              = user.getId();
        this.name            = user.getName();
        this.surname         = user.getSurname();
        this.telephoneNumber = user.getTelephoneNumber();
        this.email           = user.getEmail();
        this.password        = user.getPassword();
    }
}
