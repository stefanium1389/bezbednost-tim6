package bezbednosttim6.dto;

import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public class CodeAndRenewPasswordsDTO {

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[^\\s]).{8,}$\n", message = "Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one number and one special character.")
    private String newPassword;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[^\\s]).{8,}$\n", message = "Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one number and one special character.")
    private String repeatPassword;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[^\\s]).{8,}$\n", message = "Password must be at least 8 characters long, contain at least one uppercase and lowercase letter, one number and one special character.")
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
