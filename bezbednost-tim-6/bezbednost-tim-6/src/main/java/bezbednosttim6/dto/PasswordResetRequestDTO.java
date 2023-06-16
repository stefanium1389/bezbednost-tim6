package bezbednosttim6.dto;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Validated
public class PasswordResetRequestDTO {

	@Length(max = 100)
	@Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Bad email format")
	private String email;
	private String mode;

	public PasswordResetRequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PasswordResetRequestDTO(String email, String mode) {
		super();
		this.email = email;
		this.mode = mode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	

}
