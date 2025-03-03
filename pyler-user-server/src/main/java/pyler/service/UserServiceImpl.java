package pyler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pyler.config.JwtTokenClient;
import pyler.domain.dto.*;
import pyler.domain.entity.UserEntity;
import pyler.enums.ErrorCode;
import pyler.enums.UserEnum;
import pyler.exception.CustomException;
import pyler.exception.ServiceException;
import pyler.repository.UserRepository;
import pyler.utils.PasswordUtil;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtTokenClient jwtTokenClient;

    /**
     * User 등록
     * @param userAddDTO
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public UserResDTO postUserADD(UserAddDTO userAddDTO) {
        UserEntity userEntity = userRepository.findByUserEmailAndIsDel(userAddDTO.getEmail(), false)
                .orElseThrow(() -> new ServiceException(ErrorCode.ALREADY_EXIST_USER));

        // 패스워드 암호화(알고리즘: argon2)
        String aron2Pw = passwordUtil.hashPassWord(userAddDTO.getPassword());

        int userRole = userAddDTO.getRole().equals(UserEnum.ADMIN.getName()) ? UserEnum.ADMIN.getCode() : UserEnum.USER.getCode();

        UserEntity user = UserEntity.builder()
                .userName(userAddDTO.getName())
                .userEmail(userAddDTO.getEmail())
                .passWord(aron2Pw)
                .userRole(userRole)
                .build();

        UserEntity savedUser = userRepository.save(user);

        return UserResDTO.builder()
                .id(savedUser.getId())
                .name(savedUser.getUserName())
                .email(savedUser.getUserEmail())
                .build();

    }

    /**
     * User login
     * @param userLoginDTO
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public UserResDTO getUserLogin(UserLoginDTO userLoginDTO) {
        //사용자 조회
        UserEntity userEntity = userRepository.findByUserEmailAndIsDel(userLoginDTO.getEmail(), false)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        // 패스워드 검증
        if(!passwordUtil.verifyPassword(userLoginDTO.getPassword(), userEntity.getPassWord()))
            throw new ServiceException(ErrorCode.INVALID_USER_PASSWORD);

        // Token 생성
        TokenDTO tokenDTO = jwtTokenClient.createToken(
                userEntity.getId(),
                userEntity.getUserEmail(),
                userEntity.getUserRole()
        );

        return UserResDTO.builder()
                .id(userEntity.getId())
                .name(userEntity.getUserName())
                .email(userEntity.getUserEmail())
                .role(userEntity.getUserRole())
                .accessToken(tokenDTO.getAccessToken())
                .refreshToken(tokenDTO.getRefreshToken())
                .build();
    }
}
