package it.triolod.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.triolod.userservice.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
