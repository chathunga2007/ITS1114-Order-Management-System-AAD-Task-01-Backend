package lk.ijse.OrderManagementSystem.controller;

import java.util.List;
import lk.ijse.OrderManagementSystem.dto.AuthDTO;
import lk.ijse.OrderManagementSystem.dto.UserDataDTO;
import lk.ijse.OrderManagementSystem.security.JwtUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lk.ijse.OrderManagementSystem.constant.CommonResponse;
import static lk.ijse.OrderManagementSystem.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.OrderManagementSystem.constant.ResponseStatusCode.OPERATION_SUCCESS;
import lk.ijse.OrderManagementSystem.dto.UserDTO;
import lk.ijse.OrderManagementSystem.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveUser(@RequestBody UserDTO userDTO) {
        userService.saveUser(userDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateUser(@RequestBody UserDTO userDTO) {
        UserDTO userDTOs = userService.updateUser(userDTO);
        return new CommonResponse(OPERATION_SUCCESS, userDTOs, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUserDetails() {
        List<UserDTO> userDTOS = userService.getAllUsers();
        return new CommonResponse(OPERATION_SUCCESS, userDTOS, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUserDetail(@PathVariable Long userId) {
        UserDTO userDTO = userService.getAllUser(userId);
        return new CommonResponse(OPERATION_SUCCESS, userDTO, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse login(@RequestBody AuthDTO authDTO) {
        UserDTO userDetails = userService.getUserDetails(authDTO.getUserName(), authDTO.getPassword());
        System.out.println("Login API called for username : " + authDTO.getUserName());
        String token = jwtUtil.generateToken(userDetails);

        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setUserId(userDetails.getUserId());
        userDataDTO.setToken(token);

        return new CommonResponse(0, userDataDTO, "JWT Token");
    }
}