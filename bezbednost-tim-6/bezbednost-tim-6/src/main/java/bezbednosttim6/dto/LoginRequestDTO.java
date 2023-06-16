package bezbednosttim6.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Validated
public class LoginRequestDTO {
	@Length(max = 100)
	@Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Bad email format")
	private String email;
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[^\\s]).{8,}$\n", message = "Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one number and one special character.")
	private String password;
	
	public LoginRequestDTO() {
		super();
	}
	
	public LoginRequestDTO(String email, String password) {
		super();
		this.email = email;
		this.password = password;
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
	
	
}
