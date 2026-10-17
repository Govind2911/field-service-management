package com.FieldServiceManagement.Cloud;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;


@Configuration
public class CloudinaryConfig {
	
  @Value("${cloudinary.cloud-name}")
  private String name;
  
  @Value("${cloudinary.api-key}")
  private String key;
  
  @Value("${cloudinary.api-secret}")
  private String secret;
    

  @Bean
  public Cloudinary cloudinary() {
      System.out.println("Cloudinary cloud=" + name + " keyLength=" + (key == null ? "null" : key.length()));
      if (key == null || key.isBlank() || secret == null || secret.isBlank())
          throw new IllegalStateException("Cloudinary credentials are missing");

      Map<String, Object> config = new HashMap<>();
      config.put("cloud_name", name.trim());
      config.put("api_key", key.trim());
      config.put("api_secret", secret.trim());
      config.put("secure", true);
      return new Cloudinary(config);
  }
}
