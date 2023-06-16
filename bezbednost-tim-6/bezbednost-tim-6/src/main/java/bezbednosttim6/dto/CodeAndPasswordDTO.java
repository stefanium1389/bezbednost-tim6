package bezbednosttim6.dto;

import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class CodeAndPasswordDTO {

	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[^\\s]).{8,}$\n", message = "Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one number and one special character.")
	private String newPassword;
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[^\\s]).{8,}$\n", message = "Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one number and one special character.")
	private String repeatPassword;
	private String code;
	public CodeAndPasswordDTO(String newPassword, String repeatPassword, String code) {
		super();
		this.newPassword = newPassword;
		this.repeatPassword = repeatPassword;
		this.code = code;
	}
	public CodeAndPasswordDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRepeatPassword() {
		return repeatPassword;
	}
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
	
	

}
