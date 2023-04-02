package bezbednosttim6.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Entity
@Table(name = "certificate_types")
public class CertificateType implements Serializable {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@Column(name="name")
	String name;

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	@JsonIgnore
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Role role)) return false;
//
//        if (getId() != null ? !getId().equals(role.getId()) : role.getId() != null) return false;
//        return getName() != null ? getName().equals(role.getName()) : role.getName() == null;
		CertificateType r = (CertificateType) o;
		return (r.getId().equals(getId()) && r.getName().equals(getName()));
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getName() != null ? getName().hashCode() : 0);
		return result;
	}
}

