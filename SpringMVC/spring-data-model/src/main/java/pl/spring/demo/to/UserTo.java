package pl.spring.demo.to;

import pl.spring.demo.enumerations.UserRole;

public class UserTo {
	private Long id;
	private String userName;
	private String password;
	private UserRole role;

	public UserTo() {
	}

	public UserTo(Long id, String user, String password, UserRole role) {
		this.id = id;
		this.userName = user;
		this.password = password;
		this.setRole(role);
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
