package bezbednosttim6.dto;

public class CodeAndRenewPasswordsDTO {

    private String newPassword;
    private String repeatPassword;
    private String oldPassword;
    private String code;
    public CodeAndRenewPasswordsDTO(String newPassword, String repeatPassword, String code, String oldPassword) {
        super();
        this.newPassword = newPassword;
        this.repeatPassword = repeatPassword;
        this.oldPassword = oldPassword;
        this.code = code;
    }
    public CodeAndRenewPasswordsDTO() {
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
