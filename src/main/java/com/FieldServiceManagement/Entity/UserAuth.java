package com.FieldServiceManagement.Entity;


import java.util.Date;

import org.jspecify.annotations.Nullable;

import com.FieldServiceManagement.DTO.RegisterRequestDTO;
import com.FieldServiceManagement.ENUM.Role;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="user_auth")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserAuth {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String userName;
	
	@Column(nullable=false,unique=true)
	private String userEmail;
	
	private String phone;
	
	@Column(nullable=false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	private String resetToken;
	private Date resetTokenExpiry;
	
//
//	public UserAuth() {}
//	public UserAuth(Long id,
//			String userName,
//			String userEmail,
//			String phone,
//			String password,
//			Role role) {
//		this.id=id;
//		this.userName=userName;
//		this.userEmail=userEmail;
//		this.phone=phone;
//		this.password=password;
//		this.role=role;
//	}
//	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public Date getResetTokenExpiry() {
		return resetTokenExpiry;
	}

	public void setResetTokenExpiry(Date resetTokenExpiry) {
		this.resetTokenExpiry = resetTokenExpiry;
	}
		
}