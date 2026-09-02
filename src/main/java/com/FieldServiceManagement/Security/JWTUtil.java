package com.FieldServiceManagement.Security;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.FieldServiceManagement.ENUM.Permissions;
import com.FieldServiceManagement.Entity.UserAuth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	
	private final Key key;
	private final long validTokenTime=12*60*60*1000L;
	
	public JWTUtil() {
		String secret=System.getenv("JWT_SECRET");
		if(secret==null || secret.isEmpty()) {
			secret="Replace this place with secret code";
		}
		
		key= Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	public String generateToken(UserAuth user) {
		
		Map<String,Object>claims=new HashMap<>();
		claims.put("role", user.getRole().name());
		
		Set<Permissions> perm = RoleBasedPermissions.getRoleBasedPermission()
		        .get(user.getRole());
		
		Date now= new Date();
		Date expire=new Date(now.getTime()+validTokenTime);
		
		return Jwts.builder().
				setClaims(claims).
				setSubject(user.getUserEmail())
				.setIssuedAt(now).
				setExpiration(expire)
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
	}
	
	public boolean validateToken(String token) {
		try {
			
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
	
			
     public Claims getClaim(String token) {
    	 return Jwts.parserBuilder()
    			 .setSigningKey(key)
    			 .build()
    			 .parseClaimsJws(token)
    			 .getBody();
     }
     
     public String getUserEmail(String token) {
    	 return getClaim(token) .getSubject();
     }
     
     public String extractToken(String header) {
    	 if(header !=null && header.startsWith("Bearer ")) {
    		 return header.substring(7);
    	 }
    	 return null;
     }
}