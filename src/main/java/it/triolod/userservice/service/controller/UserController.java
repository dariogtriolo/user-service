package it.triolod.userservice.service.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import it.triolod.userservice.model.User;
import it.triolod.userservice.repository.UserRepository;
import it.triolod.userservice.service.controller.exception.UserNotFoundException;

@RestController
class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

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

	@GetMapping("/users/search")
	List<User> search(@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "surname", required = false) String surname) {

		if (name != null && !name.equalsIgnoreCase("") && surname != null && !surname.equalsIgnoreCase("")) {

			return repository.findByNameAndSurname(name, surname);
		}

		return repository.findByNameOrSurname(name, surname);
	}

	@PostMapping("/users/upload-csv")
	void uploadCSVFile(@RequestParam("file") MultipartFile file) throws Exception {

		if (file.isEmpty()) {

			log.info("File is empty");
		} else {
			try {
				Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

				CsvToBean<User> csvToBean = new CsvToBeanBuilder(reader).withType(User.class).build();

				List<User> users = csvToBean.parse();

				if (users != null && !users.isEmpty()) {
					users.forEach(repository::save);
				}

			} catch (Exception e) {
				log.error("Error during csv reading", e.getMessage());
			}
		}
	}
}