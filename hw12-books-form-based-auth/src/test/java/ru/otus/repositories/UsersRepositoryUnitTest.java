package ru.otus.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("repository for users")
class UsersRepositoryUnitTest {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private TestEntityManager em;

	@Test
	@DisplayName("should return user by username")
	public void shouldReturnUserByUsername() {
		var user = usersRepository.findAll().get(0);

		assertThat(user).isNotNull();

		var expectedUser = em.find(User.class, user.getId());

		assertThat(expectedUser).isNotNull().isEqualToComparingFieldByField(user);
	}
}