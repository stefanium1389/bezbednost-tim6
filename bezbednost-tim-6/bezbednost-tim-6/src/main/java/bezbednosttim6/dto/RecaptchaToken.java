package bezbednosttim6.dto;

public class RecaptchaToken {
    private String token;

    public RecaptchaToken() {
    }

    public RecaptchaToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
