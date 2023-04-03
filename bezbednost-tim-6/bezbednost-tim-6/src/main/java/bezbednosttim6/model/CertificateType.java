package bezbednosttim6.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public enum CertificateType {
	ROOT, INTERMEDIATE, END
}
