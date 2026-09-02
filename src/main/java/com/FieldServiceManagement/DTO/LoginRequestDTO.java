package com.FieldServiceManagement.DTO;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LoginRequestDTO {
	
		 public String userEmail;		 
		 public String password;
}
