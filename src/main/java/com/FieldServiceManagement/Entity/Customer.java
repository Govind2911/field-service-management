package com.FieldServiceManagement.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="customer")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
 @Id
 @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;
 
 @Column(nullable=false)
 private String companyName;
 @Column(nullable=false)
 private String contactPerson;
 @Column(nullable = false, unique = true)
 private String email;
 @Column(nullable=false)
 private String phone;
 @Column(nullable=false)
 private String address;
 private boolean active;
 private LocalDateTime createdAt;
 
 @OneToMany(mappedBy="customer")
 private List<Site>sites = new ArrayList<>();
 
 public String getAddress() {
	return address;
}
 public void setAddress(String address) {
	this.address = address;
 }
 public boolean isActive() {
	return active;
 }
 public void setActive(boolean active) {
	this.active = active;
 }
 public LocalDateTime getCreatedAt() {
	return createdAt;
 }
 public void setCreatedAt(LocalDateTime createdAt) {
	this.createdAt = createdAt;
 }
 public Long getId() {
	return id;
 }
 public void setId(Long id) {
	this.id = id;
 }
 public String getCompanyName() {
	return companyName;
 }
 public void setCompanyName(String companyName) {
	this.companyName = companyName;
 }
 public String getContactPerson() {
	return contactPerson;
 }
 public void setContactPerson(String contactPerson) {
	this.contactPerson = contactPerson;
 }
 public String getEmail() {
	return email;
 }
 public void setEmail(String email) {
	this.email = email;
 }
 public String getPhone() {
	return phone;
 }
 public void setPhone(String phone) {
	this.phone = phone;
 }
 public List<Site> getSites() {
	return sites;
 }
 public void setSites(List<Site> sites) {
	this.sites = sites;
 }
 
 
 
}
