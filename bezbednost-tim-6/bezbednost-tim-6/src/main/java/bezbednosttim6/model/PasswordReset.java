package bezbednosttim6.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="password_reset")
public class PasswordReset {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false, unique = true)
	private Long id;
	private String token;
	private String email;
	private Date timestamp;
	private Date expires;
	private String mode;
	public PasswordReset(Long id, String token, String email, Date timestamp, Date expires, String mode) {
		super();
		this.id = id;
		this.token = token;
		this.email = email;
		this.timestamp = timestamp;
		this.expires = expires;
		this.mode = mode;
	}
	public PasswordReset() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Date getExpires() {
		return expires;
	}
	public void setExpires(Date expires) {
		this.expires = expires;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
	
}
