package com.springgradlesandbox.springgradlesandox.Service;

import com.springgradlesandbox.springgradlesandox.DTO.UserDTO;
import com.springgradlesandbox.springgradlesandox.Persistence.DAO.UserDAO;
import com.springgradlesandbox.springgradlesandox.Persistence.Entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserDTO createUser(UserDTO user) {
        UserEntity savedUser = userDAO.save(UserEntity.builder()
                        .accountNumber(user.getAccountNumber())
                        .name(user.getName())
                        .memberId(user.getMemberId())
                        .memberNumber(user.getMemberNumber())
                .build());
        return UserDTO.builder()
                .id(savedUser.getId())
                .accountNumber(savedUser.getAccountNumber())
                .name(savedUser.getName())
                .memberId(user.getMemberId())
                .memberNumber(user.getMemberNumber())
                .createdDate(savedUser.getCreatedDate())
                .lastModifiedDate(savedUser.getLastModifiedDate())
                .build();
    }
}
