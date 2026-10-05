package com.FieldServiceManagement.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="sites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Site {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;
  
  private String siteName;
  private String appartmentName;
  private String floorNo;
  private String addressDetails;
  private String city;
  private String state;
  private String country;
  private Long zipCode;
  
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="CustomerId")
  private Customer customer;

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
  }

  public String getSiteName() {
	return siteName;
  }

  public void setSiteName(String siteName) {
	this.siteName = siteName;
  }

  public String getAppartmentName() {
	return appartmentName;
  }

  public void setAppartmentName(String appartmentName) {
	this.appartmentName = appartmentName;
  }

  public String getFloorNo() {
	return floorNo;
  }

  public void setFloorNo(String floorNo) {
	this.floorNo = floorNo;
  }

  public String getAddressDetails() {
	return addressDetails;
  }

  public void setAddressDetails(String addressDetails) {
	this.addressDetails = addressDetails;
  }

  public String getCity() {
	return city;
  }

  public void setCity(String city) {
	this.city = city;
  }

  public String getState() {
	return state;
  }

  public void setState(String state) {
	this.state = state;
  }

  public String getCountry() {
	return country;
  }

  public void setCountry(String country) {
	this.country = country;
  }

  public Long getZipCode() {
	return zipCode;
  }

  public void setZipCode(Long zipCode) {
	this.zipCode = zipCode;
  }

  public Customer getCustomer() {
	return customer;
  }

  public void setCustomer(Customer customer) {
	this.customer = customer;
  }
  
}
