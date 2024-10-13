package com.parishi.dao;
import com.parishi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, String>
{

    // Find a user by username
    User findByUsername(String username);

    // Add a role to a user
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_roles (username, role) VALUES (:username, :role)", nativeQuery = true)
    void addRole(@Param("username") String username,@Param("role") String role);

    // Remove a role from a user
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_roles WHERE username = :username AND role = :role", nativeQuery = true)
    void removeRole(@Param("username")String username,@Param("role") String role);
}


