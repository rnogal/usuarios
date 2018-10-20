package com.restapi.springboot.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.restapi.springboot.model.User;
import com.restapi.springboot.service.UserService;
import com.restapi.springboot.util.CustomErrorType;
import com.restapi.springboot.util.PasswordUtils;
import com.restapi.springboot.viewModel.UserDTO;

@RestController
@RequestMapping("/api")
public class RestApiController {

	@Autowired
	UserService userService; 

	// -------------------  Recupera todos los usuarios ---------------------------------------------

	@RequestMapping(value = "/user/", method = RequestMethod.GET)
	public ResponseEntity<Collection<UserDTO>> listAllUsers() {
		List<User> users = userService.findAllUsers();
		if (users.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		Collection<UserDTO> usuarios = new LinkedList();
		for(User user: users) {
			UserDTO dto = new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getSurnames(), user.getBirthdate());
			usuarios.add(dto);
		}
		return new ResponseEntity<Collection<UserDTO>>(usuarios, HttpStatus.OK);
	}

	// -------------------  Recupera un usuario ------------------------------------------

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable("id") long id) {
		User user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity(new CustomErrorType("El Usuario con id " + id + " no encontrado"), HttpStatus.NOT_FOUND);
		}
		UserDTO dto = new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getSurnames(), user.getBirthdate());
		return new ResponseEntity<UserDTO>(dto, HttpStatus.OK);
	}

	// ------------------- Crea un usuario -------------------------------------------

	@RequestMapping(value = "/user/", method = RequestMethod.POST, consumes =  MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
		if (userService.isUserExist(user)) {
			return new ResponseEntity(new CustomErrorType("No se pudo crear. El Usuario con email " + user.getEmail() + " ya existe."),HttpStatus.CONFLICT);
		}
		cypherPassword(user);
		userService.saveUser(user);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	private void cypherPassword(User user) {
        String salt = PasswordUtils.getSalt(30); 
        String securePassword = PasswordUtils.generateSecurePassword(user.getPassword(), salt);
        user.setPassword(securePassword);
        user.setSalt(salt);
	}

	// ------------------- Actualización de usuario ------------------------------------------------

	@RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {

		User currentUser = userService.findById(id);

		if (currentUser == null) {
			return new ResponseEntity(new CustomErrorType("No se pudo actualizar. Usuario con id " + id + " no encontrado."), HttpStatus.NOT_FOUND);
		}

		currentUser.setEmail(user.getEmail());
		currentUser.setBirthdate(user.getBirthdate());
		currentUser.setPassword(user.getPassword());
		currentUser.setName(user.getName());
		currentUser.setSurnames(user.getSurnames());
		
		userService.updateUser(currentUser);
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}

	// ------------------- Delete a User-----------------------------------------

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {

		User user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity(new CustomErrorType("No se pudo elimianr. Usuario con id " + id + " no encontrado."),  HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(id);
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Users-----------------------------

	@RequestMapping(value = "/user/", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers() {
		userService.deleteAllUsers();
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

}