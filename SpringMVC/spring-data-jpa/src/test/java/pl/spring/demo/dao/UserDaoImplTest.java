package pl.spring.demo.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.spring.demo.entity.UserEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "CommonDaoTest-context.xml")
public class UserDaoImplTest {

	@Autowired
	private UserDao userDao;

	@Test
	public void testShouldFindUserById() {
		// given
		final long userId = 1;
		// when
		UserEntity userEntity = userDao.findOne(userId);
		// then
		assertNotNull(userEntity);
		assertEquals("admin", userEntity.getUserName());
	}

	@Test
	public void testShouldFindUsersByName() {
		// given
		String name = "admin";
		// when
		List<UserEntity> usersEntity = userDao.findUserByName(name);
		// then
		assertNotNull(usersEntity);
		assertFalse(usersEntity.isEmpty());
		assertEquals(name, usersEntity.get(0).getUserName());
	}
}
