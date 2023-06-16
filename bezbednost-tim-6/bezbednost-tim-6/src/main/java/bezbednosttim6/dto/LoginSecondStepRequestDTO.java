package bezbednosttim6.dto;

public class LoginSecondStepRequestDTO {
	private String token;
	private String code;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LoginSecondStepRequestDTO(String token, String code) {
		this.token = token;
		this.code = code;
	}

	public LoginSecondStepRequestDTO() {
	}
}
