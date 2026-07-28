package lk.ijse.OrderManagementSystem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lk.ijse.OrderManagementSystem.exception.CustomerException;
import org.springframework.stereotype.Service;
import lk.ijse.OrderManagementSystem.dto.UserDTO;
import lk.ijse.OrderManagementSystem.entity.User;
import lk.ijse.OrderManagementSystem.repository.UserRepository;
import lk.ijse.OrderManagementSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(UserDTO userDTO) {
        log.info("Execute method saveUser()");
        if(userDTO.getRole().equals(""))
            throw new CustomerException(404, "User Roles Cannot Be Empty!" );

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        userRepository.save(user);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        log.info("Execute method updateUser()" + userDTO);
        try {
            Optional<User> optionalUser = userRepository.findById(userDTO.getUserId());

            if (!optionalUser.isPresent()/*optionalUser.isEmpty()*/) {
                throw new RuntimeException("User not found");
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
        } catch (Exception e) {
            log.error("Error is updatedUser" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Execute method getUsers()");
        try {
            log.info("Get Users ...");
            List<UserDTO> responseList = new ArrayList<>();
            List<User> usersList = userRepository.findAll();
            for (User user : usersList) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(user.getUserId());
                userDTO.setUsername(user.getUsername());
                userDTO.setPassword(user.getPassword());
                userDTO.setRole(user.getRole());

                responseList.add(userDTO);
            }
            return responseList;
        } catch (Exception e) {
            log.error("Error in msg getUsers()" + e.getMessage());
            throw e;
        }
    }

    @Override
    public UserDTO getAllUser(Long userId) {
        log.info("Execute method getUserDetail()");
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent()) {
                throw new RuntimeException("User not found with id: " + userId);
            }
            User user = optionalUser.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setRole(user.getRole());
            log.info("User detail retrieved successfully");
            return userDTO;
        } catch (Exception e) {
            log.error("Error in getUserDetail: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public UserDTO getUserDetails(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsernameAndPassword(username, password);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("user not found");
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