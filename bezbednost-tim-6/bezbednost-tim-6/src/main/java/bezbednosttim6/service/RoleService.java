package bezbednosttim6.service;

import bezbednosttim6.exception.RoleNotFoundException;
import bezbednosttim6.model.Role;
import bezbednosttim6.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findById(Integer id) {
        return this.roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role by id " + id + " was not found"));
    }
}
