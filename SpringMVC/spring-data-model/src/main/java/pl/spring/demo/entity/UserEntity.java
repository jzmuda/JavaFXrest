package pl.spring.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.enumerations.UserRole;

@Entity
@Table(name = "USERENTITY")
public class UserEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false, length = 50)
	private String userName;
	@Column(nullable = false, length = 200)
	private String password;
	@Enumerated(EnumType.STRING)
	private UserRole role;

	// for hibernate
	protected UserEntity() {
	}

	public UserEntity(Long id, String user, String password, UserRole role) {
		this.id = id;
		this.userName = user;
		this.password = password;
		this.role= role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String user) {
		this.userName = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
}
