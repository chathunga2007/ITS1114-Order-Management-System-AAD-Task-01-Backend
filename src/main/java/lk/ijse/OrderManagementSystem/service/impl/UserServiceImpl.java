package lk.ijse.OrderManagementSystem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lk.ijse.OrderManagementSystem.dto.UserDTO;
import lk.ijse.OrderManagementSystem.entity.User;
import lk.ijse.OrderManagementSystem.exception.CustomException;
import lk.ijse.OrderManagementSystem.repository.UserRepository;
import lk.ijse.OrderManagementSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(UserDTO userDTO) {
        log.info("Execute method saveUser()");
        if (userDTO == null) {
            throw new CustomException(400, "User data cannot be null!");
        }
        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            throw new CustomException(400, "Username cannot be empty!");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw new CustomException(400, "Password cannot be empty!");
        }
        if (userDTO.getRole() == null) {
            throw new CustomException(404, "User Roles Cannot Be Empty!");
        }
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new CustomException(400, "Username already exists!");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        userRepository.save(user);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        log.info("Execute method updateUser() {}", userDTO);
        if (userDTO == null || userDTO.getUserId() == null) {
            throw new CustomException(400, "User ID cannot be null!");
        }
        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            throw new CustomException(400, "Username cannot be empty!");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw new CustomException(400, "Password cannot be empty!");
        }
        if (userDTO.getRole() == null) {
            throw new CustomException(400, "User role cannot be empty!");
        }

        Optional<User> optionalUser = userRepository.findById(userDTO.getUserId());

        if (optionalUser.isEmpty()) {
            throw new CustomException(404, "User not found with id: " + userDTO.getUserId());
        }

        User user = optionalUser.get();

        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());
        user.setPassword(userDTO.getPassword());

        User updatedUser = userRepository.save(user);

        UserDTO responseDTO = new UserDTO();

        responseDTO.setUserId(updatedUser.getUserId());
        responseDTO.setUsername(updatedUser.getUsername());
        responseDTO.setRole(updatedUser.getRole());
        responseDTO.setPassword(updatedUser.getPassword());

        log.info("User Updated ...");
        return responseDTO;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Execute method getUsers()");
        List<UserDTO> responseList = new ArrayList<>();
        List<User> usersList = userRepository.findAll();
        if (usersList.isEmpty()) {
            throw new CustomException(404, "No users found!");
        }
        for (User user : usersList) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setRole(user.getRole());

            responseList.add(userDTO);
        }
        return responseList;
    }

    @Override
    public UserDTO getAllUser(Long userId) {
        log.info("Execute method getUserDetail()");
        if (userId == null || userId <= 0) {
            throw new CustomException(400, "Invalid User ID: " + userId);
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new CustomException(404, "User not found with id: " + userId);
        }
        User user = optionalUser.get();
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        log.info("User detail retrieved successfully");
        return userDTO;
    }

    @Override
    public UserDTO getUserDetails(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new CustomException(400, "Username and Password cannot be empty!");
        }
        Optional<User> optionalUser = userRepository.findByUsernameAndPassword(username, password);
        if (optionalUser.isEmpty()) {
            throw new CustomException(404, "User not found with provided credentials!");
        }
        User user = optionalUser.get();

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());
        userDTO.setPassword(user.getPassword());

        return userDTO;
    }
}