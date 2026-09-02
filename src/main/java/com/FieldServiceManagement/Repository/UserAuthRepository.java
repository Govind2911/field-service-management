package com.FieldServiceManagement.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FieldServiceManagement.Entity.UserAuth;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth,Long> {
	
	Optional<UserAuth>findByUserEmail(String userEmail);
    Optional<UserAuth>findByResetToken(String resetToken);
}
