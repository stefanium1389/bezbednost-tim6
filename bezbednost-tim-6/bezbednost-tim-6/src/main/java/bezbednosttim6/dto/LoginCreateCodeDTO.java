package bezbednosttim6.dto;

public class LoginCreateCodeDTO {
	private String token;

	public LoginCreateCodeDTO() {
		super();
	}

	public LoginCreateCodeDTO(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
