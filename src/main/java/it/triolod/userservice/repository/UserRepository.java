package it.triolod.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.triolod.userservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	List<User> findByNameOrSurname(String name, String surnname);
	List<User> findByNameAndSurname(String name, String surnname);
}
