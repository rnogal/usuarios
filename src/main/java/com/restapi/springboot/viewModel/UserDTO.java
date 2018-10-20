package com.restapi.springboot.viewModel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserDTO {
	
	private Long id;
	private String email;
	private String name;
	private String surnames;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date birthdate;
	
	private String password;
	private String passwordValidation;
	
	
	public UserDTO(Long id, String email, String name, String surnames, Date birthdate) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.surnames = surnames;
		this.birthdate = birthdate;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurnames() {
		return surnames;
	}
	public void setSurnames(String surnames) {
		this.surnames = surnames;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordValidation() {
		return passwordValidation;
	}

	public void setPasswordValidation(String passwordValidation) {
		this.passwordValidation = passwordValidation;
	}
	
	

}
