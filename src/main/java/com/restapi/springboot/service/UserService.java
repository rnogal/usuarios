package com.restapi.springboot.service;


import java.util.List;

import com.restapi.springboot.model.User;

public interface UserService {
	
	User findById(long id);
	
	User findByEmail(String email);
	
	void saveUser(User user);
	
	void updateUser(User user);
	
	void deleteUserById(long id);

	List<User> findAllUsers();
	
	void deleteAllUsers();
	
	boolean isUserExist(User user);
	
}
