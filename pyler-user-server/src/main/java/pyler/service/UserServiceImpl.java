package pyler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pyler.client.JwtTokenClient;
import pyler.domain.dto.*;
import pyler.domain.entity.UserRoleEntity;
import pyler.domain.entity.UserUserEntity;
import pyler.enums.ErrorCode;
import pyler.enums.UserEnum;
import pyler.exception.ServiceException;
import pyler.repository.UserRepository;
import pyler.repository.UserRoleRepository;
import pyler.utils.PasswordUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtTokenClient jwtTokenClient;


    /**
     * User 등록
     * @param userAddDTO
     * @return
     */
    @Override
    @Transactional
    public UserResDTO postUserADD(UserAddDTO userAddDTO) {

        if (userAddDTO.getUserEmail().isEmpty() || userAddDTO.getUserPassword().isEmpty()) {
            throw new ServiceException(ErrorCode.EMAIL_OR_PASSWORD_IS_NOT_EXIST);
        }

        Optional<UserUserEntity> userUserEntity = userRepository.findByUserEmailAndIsDel(userAddDTO.getUserEmail(), false);
        if (userUserEntity.isPresent())
            throw new ServiceException(ErrorCode.ALREADY_EXIST_USER);

        // 패스워드 암호화(알고리즘: argon2)
        String aron2Pw = PasswordUtil.hashPassWord(userAddDTO.getUserPassword());

        boolean userRole = userAddDTO.getIsMaster();

        UserUserEntity user = UserUserEntity.builder()
                .userName(userAddDTO.getUserName())
                .userEmail(userAddDTO.getUserEmail())
                .passWord(aron2Pw)
                .isMaster(userRole)
                .isActive(true)
                .isDel(false)
                .build();

        UserUserEntity savedUser = userRepository.save(user);

        List<UserEnum> userEnums = Arrays.asList(
                UserEnum.IMAGE_UPLOAD,
                UserEnum.IMAGE_READ,
                UserEnum.IMAGE_UPDATE,
                UserEnum.IMAGE_DELETE
        );

        List<UserRoleEntity> userRoleEntities = userEnums.stream()
                .map(role -> UserRoleEntity.builder()
                        .userId(savedUser.getId())
                        .userRoleType(role)
                        .creatorId(savedUser.getId())
                        .updatorId(savedUser.getId())
                        .build())
                .collect(Collectors.toList());
        userRoleRepository.saveAll(userRoleEntities);

        return UserResDTO.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .userEmail(savedUser.getUserEmail())
                .isMaster(savedUser.getIsMaster())
                .isActive(savedUser.getIsActive())
                .isDel(savedUser.getIsDel())
                .build();

    }

    /**
     * User login
     * @param userLoginDTO
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TokenDTO postUserLogin(UserLoginDTO userLoginDTO) {
        //사용자 조회
        Optional<UserUserEntity> userUserEntity = userRepository.findByUserEmailAndIsDel(userLoginDTO.getEmail(), false);
        if (userUserEntity.isEmpty())
            throw new ServiceException(ErrorCode.USER_NOT_FOUND);

        // 패스워드 검증
        if(!PasswordUtil.verifyPassword(userLoginDTO.getPassword(), userUserEntity.get().getPassWord()))
            throw new ServiceException(ErrorCode.INVALID_USER_PASSWORD);

        // Token 생성
        TokenDTO tokenDTO = jwtTokenClient.createToken(
                userUserEntity.get().getId(),
                userUserEntity.get().getUserEmail(),
                userUserEntity.get().getIsMaster()
        );

        return TokenDTO.builder()
                .accessToken(tokenDTO.getAccessToken())
                .refreshToken(tokenDTO.getRefreshToken())
                .build();
    }
}
