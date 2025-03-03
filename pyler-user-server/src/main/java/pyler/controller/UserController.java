package pyler.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pyler.contents.RequestMap;
import pyler.domain.dto.*;
import pyler.domain.entity.UserEntity;
import pyler.enums.ErrorCode;
import pyler.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(RequestMap.api + RequestMap.v1)
public class UserController {

    @Autowired
    private UserService userService;

    /***
     * 사용자 등록
     * @return
     */
    @PostMapping(RequestMap.user + "/add")
    public ResponseEntity<?> postUserAdd(@Valid @RequestBody UserAddDTO userAddDTO) {
        UserResDTO userResDTO = userService.postUserADD(userAddDTO);
        return ResEntity.success(userResDTO);
    }

    /***
     * 로그인 및 Token 발급
     * @param userLoginDTO
     * @return
     */
    @GetMapping(RequestMap.user + "/login")
    public ResponseEntity<?> getUserLogin(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        UserResDTO userResDTO = userService.getUserLogin(userLoginDTO);
        return ResEntity.success(userResDTO);
    }
}
