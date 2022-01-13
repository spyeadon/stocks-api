package com.springgradlesandbox.springgradlesandox.Service;

import com.springgradlesandbox.springgradlesandox.Persistence.DAO.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
}
