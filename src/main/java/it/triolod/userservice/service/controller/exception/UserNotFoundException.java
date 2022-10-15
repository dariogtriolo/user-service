package it.triolod.userservice.service.controller.exception;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(Long id) {
		super("Could not find user " + id);
	}
}
