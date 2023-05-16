package bezbednosttim6.dto;

public class CodeAndPasswordDTO {
	
	private String newPassword;
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
