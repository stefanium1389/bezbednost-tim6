package bezbednosttim6.mapper;

import bezbednosttim6.dto.RegisterRequestDTO;
import bezbednosttim6.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDTOwithPasswordMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public UserDTOwithPasswordMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User fromDTOtoUser(RegisterRequestDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    public RegisterRequestDTO fromUserToDTO(User dto) {
        return modelMapper.map(dto, RegisterRequestDTO.class);
    }
}
