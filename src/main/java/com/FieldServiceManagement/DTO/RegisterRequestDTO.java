package com.FieldServiceManagement.DTO;

import com.FieldServiceManagement.ENUM.Role;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

	 public String userName;
	 public String userEmail;
	 public String phone;
	 public String password;
	 public Role role;
}
