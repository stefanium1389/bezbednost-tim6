package bezbednosttim6.dto;

public class CodeAndPasswordDTO {
	
	private String newPassword;
	private String code;
	public CodeAndPasswordDTO(String newPassword, String code) {
		super();
		this.newPassword = newPassword;
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
	
	

}
