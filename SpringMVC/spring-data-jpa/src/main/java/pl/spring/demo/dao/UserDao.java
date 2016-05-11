package pl.spring.demo.dao;

import java.util.List;

import pl.spring.demo.entity.UserEntity;

public interface UserDao extends Dao<UserEntity, Long> {

	List<UserEntity> findUserByName(String name);
}
