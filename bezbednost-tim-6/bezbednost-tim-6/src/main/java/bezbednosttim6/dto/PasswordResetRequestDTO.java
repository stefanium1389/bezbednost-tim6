package bezbednosttim6.dto;

public class PasswordResetRequestDTO {
	
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
