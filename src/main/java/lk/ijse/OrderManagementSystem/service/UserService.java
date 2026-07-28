package lk.ijse.OrderManagementSystem.service;

import java.util.List;

import lk.ijse.OrderManagementSystem.dto.UserDTO;

public interface UserService {
    void saveUser(UserDTO userDTO);
    UserDTO updateUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getAllUser(Long userId);
    UserDTO getUserDetails(String username, String password);
}
