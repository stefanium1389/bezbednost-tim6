package bezbednosttim6.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="users")
@Entity
public class User implements UserDetails, Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false, unique = true)
	private Long id;
	private String name;
	private String surname;
	@Lob
	private byte[] profilePicture;
	private String telephoneNumber;
	private String email;
	private String address;
	private String password;
	private boolean activated;
	private boolean blocked;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonManagedReference
	private Role role;

	@Transient
	private String accessToken;

	public User() {
		super();

	}

	public User(Long id, String name, String surname, byte[] profilePicture, String telephoneNumber, String email,
			String address, String password, boolean activated, boolean blocked, Role role) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.profilePicture = profilePicture;
		this.telephoneNumber = telephoneNumber;
		this.email = email;
		this.address = address;
		this.password = password;
		this.activated = activated;
		this.blocked = blocked;
		this.role = role;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public byte[] getProfilePicture() {
		return profilePicture;
	}

	public String getProfilePictureAsString() {
		if (this.profilePicture == null)
			return null;
		String encodedString = Base64.getEncoder().encodeToString(this.profilePicture);
		StringBuilder sb = new StringBuilder(encodedString);

		sb.insert(4, ":");
		sb.insert(15, ";");
		sb.insert(22, ",");

		String newString = sb.toString();
		return newString;
	}

	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", surname=" + surname + ", profilePicture=" + profilePicture
				+ ", telephoneNumber=" + telephoneNumber + ", email=" + email + ", address=" + address + ", password="
				+ password + ", role=" + role.toString() + ", accessToken=" + accessToken + "]";
	}


	private byte[] convertToByte(String string)
	{
		byte[] decodedBytes = Base64.getMimeDecoder().decode(string);


		return decodedBytes;
	}

	public void setProfilePicture(String profilePicture2) {
		this.profilePicture = convertToByte(profilePicture2);

	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	/******************************************************/
	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<Role> roles = new ArrayList<Role>();
		roles.add(this.role);
		return roles;
	}
	@JsonIgnore
	@Override
	public String getUsername() {
		return email;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
}
