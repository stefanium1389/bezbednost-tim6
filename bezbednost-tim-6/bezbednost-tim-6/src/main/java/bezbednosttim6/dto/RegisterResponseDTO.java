package bezbednosttim6.dto;

import bezbednosttim6.model.User;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public class RegisterResponseDTO {
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


    public RegisterResponseDTO() {
    }

    public RegisterResponseDTO(Long id, String name, String surname, String telephoneNumber, String email) {
        this.id                 = id;
        this.name               = name;
        this.surname            = surname;
        this.telephoneNumber    = telephoneNumber;
        this.email              = email;
    }


    public RegisterResponseDTO(User user) {
        this.id              = user.getId();
        this.name            = user.getName();
        this.surname         = user.getSurname();
        this.telephoneNumber = user.getTelephoneNumber();
        this.email           = user.getEmail();
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
}

