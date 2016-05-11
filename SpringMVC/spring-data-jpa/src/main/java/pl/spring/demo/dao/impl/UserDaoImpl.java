package pl.spring.demo.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import pl.spring.demo.dao.UserDao;
import pl.spring.demo.entity.UserEntity;

public class UserDaoImpl extends AbstractDao<UserEntity, Long> implements UserDao {

	@Override
	public List<UserEntity> findUserByName(String name) {
		TypedQuery<UserEntity> query = entityManager.createQuery(
				"select user from UserEntity user where upper(user.userName) like concat(upper(:name), '%')",
				UserEntity.class);
		query.setParameter("name", name);
		return query.getResultList();
	}
}
