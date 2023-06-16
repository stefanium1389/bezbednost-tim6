package bezbednosttim6.dto;

import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class ChangePasswordRequestDTO {

	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[^\\s]).{8,}$\n", message = "Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one number and one special character.")
	private String newPassword;
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[^\\s]).{8,}$\n", message = "Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one number and one special character.")
	private String oldPassword;
	public ChangePasswordRequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChangePasswordRequestDTO(String newPassword, String oldPassword) {
		super();
		this.newPassword = newPassword;
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	

}
