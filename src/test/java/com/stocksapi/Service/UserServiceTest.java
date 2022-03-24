package com.stocksapi.Service;

import com.stocksapi.DTO.UserDTO;
import com.stocksapi.Persistence.DAO.UserDAO;
import com.stocksapi.Persistence.Entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    private UserDTO unsavedUserDTO;
    private UserEntity savedUser;
    private final String id = UUID.randomUUID().toString();

    @BeforeEach
    void setup() {
        unsavedUserDTO = UserDTO.builder()
                .firstName("tester")
                .lastName("testerson")
                .username("test123")
                .password("pa55w0rd098")
                .build();

        savedUser = UserEntity.builder()
                .id(id)
                .firstName("tester")
                .lastName("testerson")
                .username("test123")
                .password("pa55w0rd098")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    @Test
    void createUser_savesWithUserDAO_returnsSavedUser() {
        given(userDAO.save(any(UserEntity.class))).willReturn(savedUser);

        UserDTO savedUserDTO = userService.createUser(unsavedUserDTO);

        assertThat(savedUserDTO.getFirstName()).isEqualTo("tester");
        assertThat(savedUserDTO.getLastName()).isEqualTo("testerson");
        assertThat(savedUserDTO.getUsername()).isEqualTo("test123");
        assertThat(savedUserDTO.getPassword()).isEqualTo("pa55w0rd098");
        assertThat(savedUserDTO.getId()).matches(UUID_REGEX_PATTERN);
        assertThat(savedUserDTO.getCreatedDate()).isBefore(LocalDateTime.now());
        assertThat(savedUserDTO.getLastModifiedDate()).isBefore(LocalDateTime.now());

        then(userDAO).should().save(any(UserEntity.class));
    }

    private final static Pattern UUID_REGEX_PATTERN =
            Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");
}