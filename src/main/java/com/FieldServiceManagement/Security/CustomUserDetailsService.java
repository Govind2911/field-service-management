package com.FieldServiceManagement.Security;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.FieldServiceManagement.ENUM.Permissions;
import com.FieldServiceManagement.Entity.UserAuth;
import com.FieldServiceManagement.Repository.UserAuthRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthRepository userRepo;

    public CustomUserDetailsService(UserAuthRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) {

        UserAuth user = userRepo.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Permissions> perms =
                RoleBasedPermissions.getRoleBasedPermission()
                        .get(user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getPassword(),
                null
        );
    }
}