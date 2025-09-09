package org.expensetracker.repositories;

import org.expensetracker.model.users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<users,Integer> {

    @Query("SELECT u FROM users u WHERE u.username = :username")
    users findByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("INSERT INTO users (username, password_hash) VALUES (:username, :password)")
    void addNewUser(@Param("username") String username, @Param("password") String password);
}
