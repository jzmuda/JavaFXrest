package pl.spring.demo.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.spring.demo.entity.UserEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "CommonRepositoryTest-context.xml")
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testShouldFindUserById() {
		// given
		final long userId = 1;
		// when
		UserEntity userEntity = userRepository.findOne(userId);
		// then
		assertNotNull(userEntity);
		assertEquals("admin", userEntity.getUserName());
	}

	@Test
	public void testShouldFindUsersByTitle() {
		// given
		final String userName = "admin";
		// when
		List<UserEntity> usersEntity = userRepository.findUsersByName(userName);
		// then
		assertNotNull(usersEntity);
		assertFalse(usersEntity.isEmpty());
		assertEquals(userName, usersEntity.get(0).getUserName());
	}
}
