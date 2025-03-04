package pyler.service;

import pyler.domain.dto.TokenDTO;
import pyler.domain.dto.UserAddDTO;
import pyler.domain.dto.UserLoginDTO;
import pyler.domain.dto.UserResDTO;

public interface UserService {

    UserResDTO postUserADD(UserAddDTO userAddDTO);

    TokenDTO postUserLogin(UserLoginDTO userLoginDTO);

}
