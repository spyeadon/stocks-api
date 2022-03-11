package com.stocksapi.Persistence.DAO;

import com.stocksapi.Persistence.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<UserEntity, String> {
}
