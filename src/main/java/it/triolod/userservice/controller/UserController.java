package it.triolod.userservice.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import it.triolod.userservice.controller.exception.UserNotFoundException;
import it.triolod.userservice.model.User;
import it.triolod.userservice.repository.UserRepository;

@RestController
@RequestMapping(path = "/api")
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
	ResponseEntity<User> newUser(@Valid @RequestBody User user) {

		User result = repository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}

	@GetMapping("/users/{id}")
	User getUser(@PathVariable Long id) {

		return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	@PutMapping("/users/{id}")
	User updateUser(@RequestBody User newUser, @PathVariable Long id) {

		return repository.findById(id).map(user -> {
			user.setName(newUser.getName());
			user.setSurname(newUser.getSurname());
			user.setEmail(newUser.getEmail());
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

	@PostMapping(path = "/users/upload-csv")
	ResponseEntity<List<User>> uploadCSVFile(@RequestPart("file") MultipartFile file) throws Exception {
		List<User> createdUsers = new ArrayList<>();

		if (file.isEmpty()) {
			log.info("File is empty");			
			//throw new UploadedFileIsEmptyException();
		} else {
			try {
				Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

				CsvToBean<User> csvToBean = new CsvToBeanBuilder(reader).withType(User.class).build();

				List<User> users = csvToBean.parse();

				if (users != null && !users.isEmpty()) {
					createdUsers = repository.saveAll(users);
				}

			} catch (Exception e) {				
				log.error("Error in paring csv", e.getMessage());
				throw new Exception();
			}
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUsers);
	}
}