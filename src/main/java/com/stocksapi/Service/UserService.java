package com.stocksapi.Service;

import com.stocksapi.DTO.UserDTO;
import com.stocksapi.Persistence.DAO.UserDAO;
import com.stocksapi.Persistence.Entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserDTO createUser(UserDTO user) {
        UserEntity savedUser = userDAO.save(
            UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
            .build());

        return UserDTO.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .username(savedUser.getUsername())
                .password(savedUser.getPassword())
                .createdDate(savedUser.getCreatedDate())
                .lastModifiedDate(savedUser.getLastModifiedDate())
                .build();
    }
}
