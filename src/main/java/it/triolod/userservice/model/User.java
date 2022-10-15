package it.triolod.userservice.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.opencsv.bean.CsvBindByName;

@Entity
@Table(name = "USERS", 
uniqueConstraints = @UniqueConstraint(columnNames={"name", "surname"}, name="users_unique_constraint"))
public class User {

	@Id 
	@GeneratedValue
	private Long id;

	@CsvBindByName
	@Column(nullable = false)
	@NotBlank(message="Name cannot be blank")
	private String name;

	@CsvBindByName
	@Column(nullable = false)
	@NotBlank(message="Name cannot be blank")
	private String surname;

	@CsvBindByName
	@Email(message="Invalid email address")
	private String email;

	@CsvBindByName
	private String address;

	public User() {
	}

	public User(Long id, String name, String surname, String email, String address) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.address = address;
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", surname=" + surname + ", email=" + email + ", address="
				+ address + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(address, other.address) && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(surname, other.surname);
	}

}
