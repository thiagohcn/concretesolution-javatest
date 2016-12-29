package com.concrete.solution.java.test.repository;


import com.concrete.solution.java.test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email=:email")
    User getByEmail(@Param("email") String email);
}
