package pyler.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pyler.domain.dto.UserAddDTO;
import pyler.domain.dto.UserDTO;
import pyler.domain.dto.UserLoginDTO;
import pyler.domain.dto.UserResDTO;
import pyler.domain.entity.UserEntity;

public interface UserService {

    UserResDTO postUserADD(UserAddDTO userAddDTO);

    ResponseEntity<?> getUser(UserDTO userDTO);

    UserResDTO getUserLogin(UserLoginDTO userLoginDTO);

}
