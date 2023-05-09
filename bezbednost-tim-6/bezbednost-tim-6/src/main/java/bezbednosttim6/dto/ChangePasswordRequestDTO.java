package bezbednosttim6.dto;

public class ChangePasswordRequestDTO {
	
	private String newPassword;
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
