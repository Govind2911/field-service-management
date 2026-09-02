package com.FieldServiceManagement.Service;

import java.util.Date;
import java.util.Optional; 
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.FieldServiceManagement.DTO.AuthResponseDTO;
import com.FieldServiceManagement.DTO.LoginRequestDTO;
import com.FieldServiceManagement.DTO.RegisterRequestDTO;
import com.FieldServiceManagement.Entity.UserAuth;
import com.FieldServiceManagement.Repository.UserAuthRepository;
import com.FieldServiceManagement.Security.EmailLogService;
import com.FieldServiceManagement.Security.JWTUtil;
import com.FieldServiceManagement.Security.TokenBlockService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthService {
	
	@Autowired
	private UserAuthRepository userAuthRepo;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncode;
	
	@Autowired
	private EmailLogService emailLogService;
	
	
	@Autowired
	private TokenBlockService tokenBlockService;
	
	
	public String register(RegisterRequestDTO register) {
		
		Optional<UserAuth> existingUser= userAuthRepo.findByUserEmail(register.userEmail);
		

		if(existingUser.isPresent()) {
			throw new RuntimeException("User already exist");
		}
	
		UserAuth user= new UserAuth();
		
		user.setUserName(register.userName);
		user.setUserEmail(register.userEmail);
		user.setPassword(passwordEncode.encode(register.password));
		user.setPhone(register.phone);
		user.setRole(register.role);
		
		 userAuthRepo.save(user);
		 
		 return "User Register Successfully";
		
	}
	
	public AuthResponseDTO login(LoginRequestDTO login) {
		
		UserAuth user= userAuthRepo.findByUserEmail(login.userEmail)
				        .orElseThrow(()-> new RuntimeException("User not found"));
		
		if(!passwordEncode.matches(login.password, user.getPassword())) {
			
			throw new RuntimeException("Invalid credentials");
		}
		
		String token= jwtUtil.generateToken(user);
		
		return new AuthResponseDTO(token,"Login successfully");
		
	}
	
	
	public void forgotPassword(String userEmail) {
		
		UserAuth user= userAuthRepo.findByUserEmail(userEmail)
				.orElseThrow(()-> new RuntimeException("user not found"));
		
		String token = UUID.randomUUID().toString();
		user.setResetToken(token);
		user.setResetTokenExpiry(new Date(System.currentTimeMillis() + 10*60*1000));
		userAuthRepo.save(user);
		
		emailLogService.sendResetPasswordEmail(userEmail, token);
		
	}
	
	
	public void resetPassword(String token,String newPasswod) {
		
		UserAuth user = userAuthRepo.findByResetToken(token)
				.orElseThrow(()-> new RuntimeException("invalid token"));
		
		if(user.getResetTokenExpiry().before(new Date())) {
			throw new RuntimeException("token expired");
		}
		
		user.setPassword(passwordEncode.encode(newPasswod));
		user.setResetToken(null);
		user.setResetTokenExpiry(null);
		
		userAuthRepo.save(user);
	}
	
	
	public String logout(HttpServletRequest request) {
		
		String header = request.getHeader("Authorization");
		String token = jwtUtil.extractToken(header);
		
		if(token !=null) {
			tokenBlockService.blockListToken(token);
		}
		 return "Logged out successfully";
		
	}

}