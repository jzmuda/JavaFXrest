package pl.spring.demo.service;

import java.util.List;

import pl.spring.demo.to.UserTo;

public interface UserService {

	List<UserTo> findUserByName(String name);
}
