package it.triolod.userservice.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.triolod.userservice.model.User;
import it.triolod.userservice.repository.UserRepository;
import it.triolod.userservice.service.controller.exception.UserNotFoundException;

@RestController
class UserController {

	private final UserRepository repository;

	UserController(UserRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/users")
	List<User> getAll() {
		return repository.findAll();
	}

	@PostMapping("/users")
	User newEmployee(@RequestBody User user) {
		return repository.save(user);
	}

	@GetMapping("/users/{id}")
	User getUser(@PathVariable Long id) {

		return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	@PutMapping("/users/{id}")
	User updateUser(@RequestBody User newUser, @PathVariable Long id) {
		return repository.findById(id).map(user -> {
			user.setEmail(newUser.getName());
			user.setAddress(newUser.getAddress());
			return repository.save(user);
		}).orElseGet(() -> {
			newUser.setId(id);
			return repository.save(newUser);
		});
	}

	@DeleteMapping("/users/{id}")
	void deleteUser(@PathVariable Long id) {
		repository.deleteById(id);
	}
}