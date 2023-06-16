package bezbednosttim6.dto;

import bezbednosttim6.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Validated
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
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[^\\s]).{8,}$\n", message = "Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one number and one special character.")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidationType() {
        return validationType;
    }

    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }
}
