package com.FieldServiceManagement.Security;


import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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

        Set<GrantedAuthority> authorities = perms.stream()
                .map(permission ->
                        new SimpleGrantedAuthority(permission.name())
                )
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getPassword(),
                authorities
        );
    }
}