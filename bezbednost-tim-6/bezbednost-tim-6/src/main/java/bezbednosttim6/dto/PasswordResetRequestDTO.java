package bezbednosttim6.dto;

public class PasswordResetRequestDTO {
	
	private String email;

	public PasswordResetRequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PasswordResetRequestDTO(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
