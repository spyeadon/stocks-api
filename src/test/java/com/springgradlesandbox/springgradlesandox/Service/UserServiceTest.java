package com.springgradlesandbox.springgradlesandox.Service;

import com.springgradlesandbox.springgradlesandox.DTO.UserDTO;
import com.springgradlesandbox.springgradlesandox.Persistence.DAO.UserDAO;
import com.springgradlesandbox.springgradlesandox.Persistence.Entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    private UserDTO unsavedUserDTO;
    private UserEntity savedUser;

    @BeforeEach
    void setup() {
        unsavedUserDTO = UserDTO.builder()
                .memberId("12345")
                .memberNumber("12345678")
                .name("tester1")
                .accountNumber(123)
                .build();

        savedUser = UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .memberId("12345")
                .memberNumber("12345678")
                .name("tester1")
                .accountNumber(123)
                .build();
    }

    @Test
    void createUser_savesWithUserDAO_returnsSavedUser() {

        when(userDAO.save(any(UserEntity.class))).thenReturn(savedUser);

        UserDTO savedUserDTO = userService.createUser(unsavedUserDTO);

        assertThat(savedUserDTO.getAccountNumber()).isEqualTo(123);
        assertThat(savedUserDTO.getMemberId()).isEqualTo("12345");
        assertThat(savedUserDTO.getMemberNumber()).isEqualTo("12345678");
        assertThat(savedUserDTO.getName()).isEqualTo("tester1");
        assertThat(savedUserDTO.getId()).matches(UUID_REGEX_PATTERN);

        savedUser.setId(null);
        verify(userDAO, times(1)).save(savedUser);
    }

    private final static Pattern UUID_REGEX_PATTERN =
            Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");
}