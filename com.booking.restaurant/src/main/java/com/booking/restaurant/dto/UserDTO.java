// 文件路徑：src/main/java/com/booking/restaurant/dto/UserDTO.java
package com.booking.restaurant.dto;

public class UserDTO {
    private String username;
    private String email;
    private String PasswordHash;
//    private String area;
    private String phoneNumber;
    private String userType;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswordHash() {
		return PasswordHash;
	}
	public void setPasswordHash(String PasswordHash) {
		this.PasswordHash = PasswordHash;
	}
//	public String getArea() {
//		return area;
//	}
//	public void setArea(String area) {
//		this.area = area;
//	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Override
	public String toString() {
		return "UserDTO [username=" + username + ", email=" + email + ", PasswordHash=" + PasswordHash
				+ ", phoneNumber=" + phoneNumber + ", userType=" + userType + "]";
	}

    // Getters and Setters
    
}
