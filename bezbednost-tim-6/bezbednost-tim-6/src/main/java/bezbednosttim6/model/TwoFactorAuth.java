package bezbednosttim6.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="two_factor_auths")
public class TwoFactorAuth {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false, unique = true)
	private Long id;
	private String token;
	private String code;
	private String email;
	private Date timestamp;
	private Date expires;

	public TwoFactorAuth() {
		super();
	}

	public TwoFactorAuth(String token, String code, String email, Date timestamp, Date expires) {
		this.token = token;
		this.code = code;
		this.email = email;
		this.timestamp = timestamp;
		this.expires = expires;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
	
	
	
	
}
