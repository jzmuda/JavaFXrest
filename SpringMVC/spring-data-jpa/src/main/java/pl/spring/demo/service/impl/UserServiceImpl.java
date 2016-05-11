package pl.spring.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.spring.demo.entity.UserEntity;
import pl.spring.demo.mapper.UserMapper;
import pl.spring.demo.repository.UserRepository;
import pl.spring.demo.service.UserService;
import pl.spring.demo.to.UserTo;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<UserTo> findUserByName(String name) {
		List<UserEntity> entity = userRepository.findUsersByName(name);
		return UserMapper.map2To(entity);
	}

}
